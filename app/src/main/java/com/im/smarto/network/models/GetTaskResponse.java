package com.im.smarto.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public class GetTaskResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("group_id")
    private int groupId;
    @SerializedName("task_type_id")
    private int taskType;
    @SerializedName("task_descr")
    private String taskDescription;
    @SerializedName("target_date")
    private String date;
    @SerializedName("order_num")
    private int orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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
}
