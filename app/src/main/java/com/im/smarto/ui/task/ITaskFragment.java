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

    void initRecyclerView();

    void update(List<TaskGroup> data);

    void showAddActivity();

    void updateGroupAmount(int size);

    void showGroupActivity(int position);

    void showSnackBar();

    void restoreItem(TaskGroup removedItem, int removedIndex);

    void removeItem(int deletedIndex);

    void showAddGroupDialog();

    void addItem(int id, String groupName);

    void showNetworkError();
}
