package com.im.smarto.ui.task;

import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public interface TaskLoadListener {

    void onSuccess(List<TaskGroup> list);

}
