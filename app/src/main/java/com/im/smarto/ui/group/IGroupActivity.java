package com.im.smarto.ui.group;

import com.im.smarto.ui.base.BaseView;
import com.im.smarto.ui.task.model.SingleTask;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 22.03.2018.
 */

public interface IGroupActivity extends BaseView {
    void setGroupName(String groupName);

    void setupRecyclerView();

    void updateList(List<SingleTask> singleTaskList);

    void showSnackBar();

    void restoreItem(SingleTask removedItem, int removedIndex);

    void removeItem(int deletedIndex);

    void setupToolbar();

    void showAddTaskActivity(int position);

    void scrollToPosition(int position);

    void showOptionsDialog();
}
