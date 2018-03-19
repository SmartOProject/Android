package com.im.smarto.ui.task.model;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class SingleTask {

    private String mTaskText;
    private long mId;
    private int meetingUserId;
    private int mTaskType;
    private boolean isChecked;
    private int orderNum;
    private String mDate;

    public SingleTask(int id, int type, String description){
        this.mId = id;
        this.mTaskType = type;
        this.mTaskText = description;
    }

    public SingleTask(int id, int meetingUserId, int type, String description){
        this.mId = id;
        this.mTaskType = type;
        this.mTaskText = description;
        this.meetingUserId = meetingUserId;
    }

    public SingleTask(int id, int type, String description, String date){
        this.mId = id;
        this.mTaskType = type;
        this.mTaskText = description;
        this.mDate = date;
    }

    public SingleTask(int id, int meetingUserId, int type, String description, String date){
        this.mId = id;
        this.meetingUserId = meetingUserId;
        this.mTaskType = type;
        this.mTaskText = description;
        this.mDate = date;
    }

    public SingleTask(){}

    public String getTaskText() {
        return mTaskText;
    }

    public void setTaskText(String taskText) {
        mTaskText = taskText;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getTaskType() {
        return mTaskType;
    }

    public void setTaskType(int taskType) {
        mTaskType = taskType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public int getMeetingUserId() {
        return meetingUserId;
    }

    public void setMeetingUserId(int meetingUserId) {
        this.meetingUserId = meetingUserId;
    }
}
