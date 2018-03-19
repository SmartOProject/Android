package com.im.smarto.ui.task;

import com.im.smarto.ui.ILoading;
import com.im.smarto.ui.base.BaseView;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface ITaskFragment extends BaseView, ILoading {

    void updateList(List<TaskGroup> groupList);
    void openAddActivity();
    void setupRecyclerView();
    void showDialog(TaskGroup group, SingleTask task);

}
