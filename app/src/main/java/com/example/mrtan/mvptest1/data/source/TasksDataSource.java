package com.example.mrtan.mvptest1.data.source;

import android.support.annotation.NonNull;

import com.example.mrtan.mvptest1.data.Task;

import java.util.List;

/**
 * Main entry for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() has callback. Consider adding callbacks to other
 * methods to inform the user of network/databases error or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface TasksDataSource {

    interface LoadTasksCallback {
        void onTaskLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteTask(@NonNull String taskId);

    void deleteAllTasks();
}
