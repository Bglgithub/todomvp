package com.example.mrtan.mvptest1.tasks;

/**
 * Used with the filter spinner in the tasks list.
 */
public enum TasksFilterType {

    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     *Filter only the completed tasks.
     */
    COMPLETED_TASKS
}
