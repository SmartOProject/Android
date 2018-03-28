package com.im.smarto.ui.group.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 28.03.2018.
 */

public class TaskDiffCallback extends DiffUtil.Callback {

    private List<SingleTask> oldList;
    private List<SingleTask> newList;

    public TaskDiffCallback(List<SingleTask> oldList, List<SingleTask> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        SingleTask oldItem = oldList.get(oldItemPosition);
        SingleTask newItem = newList.get(newItemPosition);

        return (oldItem.getTaskText().equals(newItem.getTaskText()) &&
                oldItem.getTaskType() == newItem.getTaskType());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        SingleTask oldData = oldList.get(oldItemPosition);
        SingleTask newData = newList.get(newItemPosition);

        Bundle payload = new Bundle();
        if (!oldData.getTaskText().equals(newData.getTaskText())) {
            payload.putString("TASK_DESCRIPTION_CHANGE", newData.getTaskText());
        }

        if (oldData.getDate() != null && !oldData.getDate().equals(newData.getDate())) {
            payload.putString("TASK_DATE_CHANGE", newData.getDate());
        }

        if (payload.isEmpty()) {
            return null; // do full binding
        } else {
            return payload;
        }
    }

}
