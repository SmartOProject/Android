package com.android.smarto.architecture.task;

import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.architecture.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface ITaskFragment extends BaseView {

    void updateList();
    void openAddActivity();

}
