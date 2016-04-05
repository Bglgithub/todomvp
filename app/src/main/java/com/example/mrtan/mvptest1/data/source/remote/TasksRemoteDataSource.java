package com.example.mrtan.mvptest1.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.mrtan.mvptest1.data.Task;
import com.example.mrtan.mvptest1.data.source.TasksDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the data source that add a latency simulating network.
 */
public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource INSTANCE;

    private static int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Task> TASK_SERVICE_DATA;

    static {
        TASK_SERVICE_DATA = new LinkedHashMap<>();
        addTask("Build tower in pisa", "Ground looks good, no foundation work required");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the coast!");
    }

    public static TasksRemoteDataSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    //Prevent direct instantiation
    //防止直接实例化
    private TasksRemoteDataSource(){}

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASK_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is never fired, In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {

        //Simulate network by delaying the execution.模仿网络加载数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(Lists.newArrayList(TASK_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is never fired, In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        final Task task = TASK_SERVICE_DATA.get(taskId);

        //Simulate network by delaying the execution.模仿网络加载数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(task);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASK_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASK_SERVICE_DATA.put(completedTask.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

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
}
