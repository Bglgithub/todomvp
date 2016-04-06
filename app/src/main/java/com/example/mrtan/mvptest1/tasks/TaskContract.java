package com.example.mrtan.mvptest1.tasks;

import android.support.annotation.NonNull;

import com.example.mrtan.mvptest1.BasePresenter;
import com.example.mrtan.mvptest1.BaseView;
import com.example.mrtan.mvptest1.data.Task;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface TaskContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTaskError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfulSavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadTasks(boolean foreUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestTask);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }
}
