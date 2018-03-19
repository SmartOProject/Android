package com.im.smarto.ui.task.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskGroup {

    private long mId;
    private String mGroupName;
    private int orderNum;
    private List<SingleTask> mSingleTaskList;
    private long mNextChildId;

    public TaskGroup(long id, String groupName) {
        mId = id;
        mGroupName = groupName;
        mSingleTaskList = new ArrayList<>();
        mNextChildId = 0;
    }

    public TaskGroup(long id, String groupName, int orderNum) {
        mId = id;
        mGroupName = groupName;
        orderNum = orderNum;
        mSingleTaskList = new ArrayList<>();
        mNextChildId = 0;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
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
