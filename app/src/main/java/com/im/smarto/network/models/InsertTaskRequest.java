package com.im.smarto.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public class InsertTaskRequest {

    @SerializedName("group_id")
    private int groupId;
    @SerializedName("owner_user_id")
    private int meetingUserId;
    @SerializedName("task_type_id")
    private int taskTypeId;
    @SerializedName("task_descr")
    private String description;
    @SerializedName("target_date")
    private String date;
    @SerializedName("order_num")
    private int orderNum;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(int taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getMeetingUserId() {
        return meetingUserId;
    }

    public void setMeetingUserId(int meetingUserId) {
        this.meetingUserId = meetingUserId;
    }
}
