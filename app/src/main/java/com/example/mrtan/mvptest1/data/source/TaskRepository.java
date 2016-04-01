package com.example.mrtan.mvptest1.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;

import com.example.mrtan.mvptest1.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load tasks from the data source into a cache.
 *<p>
 * For simplicity, this implements a dump synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 *
 * 将数据从数据源导入到缓存的具体实现
 *
 * 简单讲：实现一个堆在远程数据和本地持久数据之间同步，在本地数据不存在或者为空的情况下从远程数据拉取数据
 */
public class TaskRepository implements TasksDataSource{

    private static TaskRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTaskLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     * 这个变量拥有包的访问权限，可供测试
     */
    Map<String, Task> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     * 用于标记缓存时候可用来强制更新下次的数据访问。 这个属性用于包访问权限，可供测试
     */
    boolean mCacheIsDirty = false;

    //Prevent direct instantiation. 私有化构造器
    private TaskRepository(@NonNull TasksDataSource taskRemoteDataSource,
                           @NonNull TasksDataSource taskLocalDataSource) {
        mTasksRemoteDataSource = taskRemoteDataSource;
        mTaskLocalDataSource = taskLocalDataSource;
    }

    /**
     * Return the sigle instance of this class, creating it if necessary.
     * @param taskRemoteDataSource the backend data source
     * @param taskLocalDataSource the device storage data source
     * @return the {@link TaskRepository} instance
     * 单例模式 不知道为什么没有使用双验证或者静态内部类
     */
    public static TaskRepository getInstance(@NonNull TasksDataSource taskRemoteDataSource,
                                             @NonNull TasksDataSource taskLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TaskRepository(taskRemoteDataSource, taskLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Get tasks from cache, local data source (SQLLite) or remote data source,whichever is
     * available first.
     *
     * Note:{@link LoadTasksCallback#onDataNotAvailable()} is fires if all data source fail
     * to get the data.
     *
     * 从缓存，本地数据源（SQLite）或远程数据源获取数据，按照顺序第一个可用数据
     * 如果所有的数据都获取失败LoadTasksCallback.onDataNotAvailable()会被调用
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache id available and not dirty
        //如果缓存中有数据并且不是脏数据，返回缓存中的数据
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            // if the cache is dirty we need to fetch new data from the network.
            // 如果数据是脏的需要从网络拉取数据
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. if not, query the network.
            //查询本地数据是否可用，不可用从网络中拉取
            mTaskLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTaskLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTaskLocalDataSource.saveTask(task);

        //Do in memory cache update to keep the UI up to data
        //强制内存更新来保持ui的更新
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.completeTask(task);
        mTaskLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        //Do in memory cache update to keep the UI up to data
        //强制内存更新来保持ui的更新
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(),completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTaskLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(),task.getDescription(), task.getId());

        //Do in memory cache update to keep the UI up to data
        //强制内存更新来保持ui的更新
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTaskLocalDataSource.clearCompletedTasks();
        mTasksRemoteDataSource.clearCompletedTasks();

        //Do in memory cache update to keep the UI up to data
        //强制内存更新来保持ui的更新
        if (mCachedTasks == null){
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()){
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }

    @Override
    public void deleteAllTasks() {

    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback){
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTaskLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks){
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task: tasks){
            mCachedTasks.put(task.getId(),task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks){
        mTaskLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTaskLocalDataSource.saveTask(task);
        }
    }

    private Task getTaskWithId(@NonNull String taskId){
        checkNotNull(taskId);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(taskId);
        }
    }

}
