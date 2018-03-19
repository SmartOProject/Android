package com.im.smarto.network.services;

import com.im.smarto.network.models.GetGroupResponse;
import com.im.smarto.network.models.GetTaskResponse;
import com.im.smarto.network.models.InsertGroupRequest;
import com.im.smarto.network.models.InsertGroupResponse;
import com.im.smarto.network.models.InsertTaskRequest;
import com.im.smarto.network.models.InsertTaskResponse;
import com.im.smarto.network.models.PutGroupRequest;
import com.im.smarto.network.models.PutTaskRequest;
import com.im.smarto.network.models.RowsAffectedResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public interface TaskService {

    @GET("task_group")
    Single<List<GetGroupResponse>> getGroupList(@Header("Authorization") String auth);

    @GET("task")
    Single<List<GetTaskResponse>> getTaskList(@Header("Authorization") String auth);

    @POST("task")
    Single<InsertTaskResponse> insertTask(@Header("Authorization") String auth,
                                          @Body InsertTaskRequest request);

    @POST("task_group")
    Single<InsertGroupResponse> insertGroup(@Header("Authorization") String auth,
                                            @Body InsertGroupRequest request);

    @DELETE("task_group/{id}")
    Single<RowsAffectedResponse> deleteGroup(@Header("Authorization") String auth,
                                             @Path("id") int id);

    @DELETE("task/{id}")
    Single<RowsAffectedResponse> deleteTask(@Header("Authorization") String auth,
                                            @Path("id") int id);

    @PUT("task/{id}")
    Single<RowsAffectedResponse> changeTask(@Header("Authorization") String auth,
                                            @Body PutTaskRequest request);

    @PUT("task_group/{id}")
    Single<RowsAffectedResponse> changeGroup(@Header("Authorization") String auth,
                                            @Body PutGroupRequest request);

}
