package com.android.smarto.architecture.task.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskGroup {

    private long mId;
    private String mGroupName;
    private List<SingleTask> mSingleTaskList;
    private long mNextChildId;
    private boolean mPinned;

    public TaskGroup(long id, String groupName) {
        mId = id;
        mGroupName = groupName;
        mSingleTaskList = new ArrayList<>();
        mNextChildId = 0;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setPinned(boolean pinnedToSwipeLeft) {
        mPinned = pinnedToSwipeLeft;
    }

    public boolean isPinned() {
        return mPinned;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public List<SingleTask> getSingleTaskList() {
        return mSingleTaskList;
    }

    public void setSingleTaskList(List<SingleTask> singleTaskList) {
        mSingleTaskList = singleTaskList;
    }

    public long generateNewChildId() {
        final long id = mNextChildId;
        mNextChildId += 1;
        return id;
    }

}
