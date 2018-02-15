package com.android.smarto.architecture.task.model;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class SingleTask {

    private String mTaskText;
    private long mId;
    private String mTaskTitle;
    private boolean mPinned;

    public SingleTask(long id, String text) {
        mTaskText = text;
        mId = id;
    }

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

    public String getTaskTitle() {
        return mTaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        mTaskTitle = taskTitle;
    }
}
