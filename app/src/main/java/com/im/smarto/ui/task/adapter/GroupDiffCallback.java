package com.im.smarto.ui.task.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 27.03.2018.
 */

class GroupDiffCallback extends DiffUtil.Callback {

    private List<TaskGroup> oldList;
    private List<TaskGroup> newList;

    public GroupDiffCallback(List<TaskGroup> oldList, List<TaskGroup> newList) {
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
        TaskGroup oldData = oldList.get(oldItemPosition);
        TaskGroup newData = newList.get(newItemPosition);

        return (oldData.getGroupName().equals(newData.getGroupName()) && oldData.getSingleTaskList()
                .equals(newData.getSingleTaskList()));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TaskGroup oldData = oldList.get(oldItemPosition);
        TaskGroup newData = newList.get(newItemPosition);

        Bundle payload = new Bundle();
        if (!oldData.getGroupName().equals(newData.getGroupName())) {
            payload.putString("GROUP_NAME_CHANGE", newData.getGroupName());
        }

        if (payload.isEmpty()) {
            return null; // do full binding
        } else {
            return payload;
        }
    }
}
