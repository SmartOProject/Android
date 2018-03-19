package com.im.smarto.network.models;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public class GroupAndTaskResponse {

    private List<GetGroupResponse> groupListResponse;
    private List<GetTaskResponse> taskListResponse;

    public List<GetGroupResponse> getGroupListResponse() {
        return groupListResponse;
    }

    public void setGroupListResponse(List<GetGroupResponse> groupListResponse) {
        this.groupListResponse = groupListResponse;
    }

    public List<GetTaskResponse> getTaskListResponse() {
        return taskListResponse;
    }

    public void setTaskListResponse(List<GetTaskResponse> taskListResponse) {
        this.taskListResponse = taskListResponse;
    }
}
