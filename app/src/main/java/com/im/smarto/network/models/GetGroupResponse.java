package com.im.smarto.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public class GetGroupResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("order_num")
    private int orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
