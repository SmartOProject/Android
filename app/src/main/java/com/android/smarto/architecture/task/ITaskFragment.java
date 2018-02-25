package com.android.smarto.architecture.task;

import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.data.TaskManager;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface ITaskFragment extends BaseView {

    void updateList();
    void openAddActivity();
    void setupRecyclerView(TaskManager taskManager);
    void showDialog(String groupName, SingleTask task);

}
