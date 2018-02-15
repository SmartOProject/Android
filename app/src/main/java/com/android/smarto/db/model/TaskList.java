package com.android.smarto.db.model;

import com.android.smarto.architecture.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 15.02.2018.
 */

public class TaskList {

    public List<TaskGroup> mTaskList;

    public TaskList(List<TaskGroup> taskList) {
        mTaskList = taskList;
    }

    public List<TaskGroup> getTaskList() {
        return mTaskList;
    }

    public void setTaskList(List<TaskGroup> taskList) {
        mTaskList = taskList;
    }
}
