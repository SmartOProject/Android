package com.im.smarto.data;

import android.util.Log;

import com.im.smarto.network.models.GetGroupResponse;
import com.im.smarto.network.models.GetTaskResponse;
import com.im.smarto.network.models.GroupAndTaskResponse;
import com.im.smarto.ui.task.TaskLoadListener;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class TaskManager {

    private static final String TAG = TaskManager.class.getSimpleName();

    public TaskGroup mLastRemovedGroup;
    public int mLastRemovedGroupPosition = -1;

    public SingleTask mLastRemovedChild;
    public long mLastRemovedChildParentGroupId = -1;
    public int mLastRemovedChildPosition = -1;

    public List<TaskGroup> mData;

    public TaskManager(){
        mData = new ArrayList<>();
    }

    public void onResponse(GroupAndTaskResponse groupAndTaskResponse, TaskLoadListener callback){
        Observable.create(e -> {
            e.onNext(initData(groupAndTaskResponse));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(d -> callback.onSuccess(mData));
    }

    private boolean initData(GroupAndTaskResponse groupAndTaskResponse) {
        List<GetGroupResponse> groupResponse = groupAndTaskResponse.getGroupListResponse();
        List<GetTaskResponse> taskResponse = groupAndTaskResponse.getTaskListResponse();

        for (GetGroupResponse group: groupResponse) {
            TaskGroup taskGroup = new TaskGroup(group.getId(), group.getGroupName(), group.getOrderNum());
            List<SingleTask> taskList = new ArrayList<>();
            for (GetTaskResponse task: taskResponse) {
                SingleTask singleTask = new SingleTask();
                singleTask.setId(task.getId());
                singleTask.setTaskText(task.getTaskDescription());
                singleTask.setTaskType(task.getTaskType());
                singleTask.setDate(task.getDate());
                singleTask.setChecked(false);
                singleTask.setOrderNum(task.getOrderNum());
                if (group.getId() == task.getGroupId()) {
                    taskList.add(singleTask);
                }
            }
            //sortTasks(taskList);
            taskGroup.setSingleTaskList(taskList);
            mData.add(taskGroup);
        }
        //sortGroups(mData);
        return true;
    }

    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).getSingleTaskList().remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).getId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    private void sortGroups(List<TaskGroup> data) {

        Collections.sort(data, (o1, o2) -> {
            if (o1.getOrderNum() == o2.getOrderNum()) return 0;
            return o1.getOrderNum() < o2.getOrderNum() ? -1 : 1;
        });

    }

    private void sortTasks(List<SingleTask> taskList) {
        Collections.sort(taskList, (o1, o2) -> {
            if (o1.getOrderNum() == o2.getOrderNum()) return 0;
            return o1.getOrderNum() < o2.getOrderNum() ? -1 : 1;
        });
    }

    public int getGroupId(String groupName){
        for(TaskGroup group: mData){
            Log.i(TAG, "group name: " + group.getGroupName() + "\n" + groupName);
            if (group.getGroupName().equals(groupName)) return (int) group.getId();
        }
        return 0;
    }

    public int getGroupPosition(String groupName) {
        for (int i=0; i<mData.size(); i++){
            if (mData.get(i).getGroupName().equals(groupName)) return i;
        }
        return 0;
    }

    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            Log.i(TAG, "GROUP");
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            Log.i(TAG, "CHILD");
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        TaskGroup group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.getSingleTaskList().size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.getSingleTaskList().size();
        }

        group.getSingleTaskList().add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

    public List<String> getGroupNames(){
        List<String> names = new ArrayList<>();
        for(TaskGroup group: mData){
            names.add(group.getGroupName());
        }
        Collections.reverse(names);
        return names;
    }

    public List<TaskGroup> getData() {
        return mData;
    }

    public void setData(List<TaskGroup> data) {
        mData = data;
    }
}
