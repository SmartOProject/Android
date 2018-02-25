package com.android.smarto.architecture.task.model;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class SingleTask {

    private String mTaskText;
    private long mId;
    private boolean mPinned;
    private int mTaskType;
    private boolean isChecked;
    private String mDate;

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

    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }

    public boolean isPinned() {
        return mPinned;
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }
}
