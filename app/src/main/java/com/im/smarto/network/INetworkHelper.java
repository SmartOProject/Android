package com.im.smarto.network;

import com.im.smarto.db.entities.User;
import com.im.smarto.network.models.AuthResponse;
import com.im.smarto.network.models.ContactPosition;
import com.im.smarto.network.models.ContactRequest;
import com.im.smarto.network.models.ContactResponse;
import com.im.smarto.network.models.GetGroupResponse;
import com.im.smarto.network.models.GetTaskResponse;
import com.im.smarto.network.models.InsertContactResponse;
import com.im.smarto.network.models.InsertGroupResponse;
import com.im.smarto.network.models.InsertTaskResponse;
import com.im.smarto.network.models.RegisterResponse;
import com.im.smarto.network.models.RowsAffectedResponse;
import com.im.smarto.network.models.UpdateUserRequest;
import com.im.smarto.network.models.UserPositionResponse;
import com.im.smarto.network.models.UserResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface INetworkHelper {

    String getHeader();
    void setHeader(String authHeader);

    Single<ContactResponse> updateContact(int id, int trustId);

    Single<RowsAffectedResponse> updateUser(String query, int changeType);

    Single<RowsAffectedResponse> changeGroup(int id, int order_num);

    Single<RowsAffectedResponse> changeTask(int id, int order_num);
    Single<RowsAffectedResponse> changeTask(int id, String descriptionOrDate, int type);

    Single<RowsAffectedResponse> deleteTask(int id);

    Single<RowsAffectedResponse> deleteGroup(int id);

    Single<InsertTaskResponse> insertTask(int groupId,
                                          int taskType,
                                          String description);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int taskType,
                                          String description,
                                          int orderNum);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int taskType,
                                          String description,
                                          String date);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int meetingUserId,
                                          int taskType,
                                          String description);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int meetingUserId,
                                          int taskType,
                                          String description,
                                          int orderNum);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int meetingUserId,
                                          int taskType,
                                          String description,
                                          String date);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int meetingUserId,
                                          int taskType,
                                          String description,
                                          String date,
                                          int orderNum);
    Single<InsertTaskResponse> insertTask(int groupId,
                                          int taskType,
                                          String description,
                                          String date,
                                          int orderNum);

    Single<InsertGroupResponse> insertGroup(String name);
    Single<InsertGroupResponse> insertGroup(String name, int orderNum);

    Single<List<GetGroupResponse>> getGroupList();
    Single<List<GetTaskResponse>> getTaskList();

    Single<List<User>> getContact();
    Single<User> getContact(int id);

    Single<List<User>> searchContactsByName(String name);
    Single<List<User>> searchContactsByPhone(String phone);

    Single<AuthResponse> authenticateUser();
    Single<RegisterResponse> registerUser(User user);

    Single<UserResponse> getCurrentUser();

    Single<InsertContactResponse> insertContact(int contactId, String contactName);
    Single<RowsAffectedResponse> deleteContact(int contactId);

    Single<UserPositionResponse> updateUserPosition(double latitude, double longitude);

    Single<List<ContactPosition>> getContactPositions();

}
