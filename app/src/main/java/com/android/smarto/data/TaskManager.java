package com.android.smarto.data;

import android.support.v4.util.Pair;

import com.android.smarto.architecture.task.TaskFragment;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.architecture.task.model.TaskGroup;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class TaskManager {

    public List<TaskGroup> mData;

    private TaskGroup mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    private SingleTask mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    @Inject
    public TaskManager() {

        mData = new ArrayList<>();

    }

    public void addTaskToGroup(SingleTask task, int groupId){

        if (groupId > mData.size() || groupId < 0) {
            mData.get(0).getSingleTaskList().add(task);
            return;
        }
        mData.get(groupId).getSingleTaskList().add(task);
    }

    public void addGroup(String name){
        int position = mData.size();
        TaskGroup group = new TaskGroup(position, name);
        mData.add(group);
    }

    public int getGroupArraySize(){
        return mData.size();
    }

    public int getChildArraySize(int groupId){
        return mData.get(groupId).getSingleTaskList().size();
    }

    public int getGroupIdByName(String name){
        for (TaskGroup group: mData){
            if (group.getGroupName().equals(name)) return (int) group.getId();
        }
        return 0;
    }

    public List<String> getGroupNames(){
        List<String> names = new ArrayList<>();
        for (TaskGroup group : mData){
            names.add(group.getGroupName());
        }
        return names;
    }

    public int getGroupIdFromName(String groupName){
        for (TaskGroup group : mData){
            if (group.getGroupName().equals(groupName)) return (int) group.getId();
        }
        return 0;
    }

    public int getGroupCount() {
        return mData.size();
    }

    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).getSingleTaskList().size();
    }

    public TaskGroup getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition);
    }

    public SingleTask getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final SingleTask children = mData.get(groupPosition).getSingleTaskList().get(childPosition);

        if (childPosition < 0 || childPosition >= mData.get(groupPosition).getSingleTaskList().size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children;
    }

    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final TaskGroup item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final TaskGroup fromGroup = mData.get(fromGroupPosition);
        final TaskGroup toGroup = mData.get(toGroupPosition);

        final SingleTask item = (SingleTask) fromGroup.getSingleTaskList().remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = ((TaskGroup) toGroup).generateNewChildId();
            item.setId(newId);
        }

        toGroup.getSingleTaskList().add(toChildPosition, item);
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

    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
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

}
