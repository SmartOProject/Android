package com.im.smarto.network;

import com.im.smarto.Constants;
import com.im.smarto.db.entities.User;
import com.im.smarto.network.models.AuthResponse;
import com.im.smarto.network.models.ContactPosition;
import com.im.smarto.network.models.ContactRequest;
import com.im.smarto.network.models.ContactResponse;
import com.im.smarto.network.models.GetGroupResponse;
import com.im.smarto.network.models.GetTaskResponse;
import com.im.smarto.network.models.InsertContactRequest;
import com.im.smarto.network.models.InsertContactResponse;
import com.im.smarto.network.models.InsertGroupRequest;
import com.im.smarto.network.models.InsertGroupResponse;
import com.im.smarto.network.models.InsertTaskRequest;
import com.im.smarto.network.models.InsertTaskResponse;
import com.im.smarto.network.models.PutGroupRequest;
import com.im.smarto.network.models.PutTaskRequest;
import com.im.smarto.network.models.RegisterResponse;
import com.im.smarto.network.models.RowsAffectedResponse;
import com.im.smarto.network.models.UpdateUserRequest;
import com.im.smarto.network.models.UserPositionRequest;
import com.im.smarto.network.models.UserPositionResponse;
import com.im.smarto.network.models.UserResponse;
import com.im.smarto.network.services.ContactsService;
import com.im.smarto.network.services.TaskService;
import com.im.smarto.network.services.UserService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class NetworkHelper implements INetworkHelper {

    public static final String TAG = NetworkHelper.class.getSimpleName();

    private final Retrofit mRetrofitClient;
    private String mAuthHeader;

    @Inject
    NetworkHelper(Retrofit retrofitClient){
          this.mRetrofitClient = retrofitClient;
    }

    @Override
    public String getHeader() {
        return mAuthHeader;
    }

    @Override
    public void setHeader(String basic) {
        mAuthHeader = basic;
    }

    @Override
    public Single<ContactResponse> updateContact(int id, int trustId) {
        ContactRequest request = new ContactRequest();
        request.setIsTrust(trustId);
        return mRetrofitClient.create(ContactsService.class).updateContact(mAuthHeader, id, request);
    }

    @Override
    public Single<RowsAffectedResponse> updateUser(String query, int changeType) {
        UpdateUserRequest request = new UpdateUserRequest();
        switch (changeType) {
            case Constants.FIRST_NAME_TYPE:
                request.setFirstName(query);
                break;
            case Constants.LAST_NAME_TYPE:
                request.setLastName(query);
                break;
            case Constants.PHONE_TYPE:
                request.setPhoneName(query);
                break;
        }

        return mRetrofitClient.create(UserService.class).updateUser(mAuthHeader, request);
    }

    @Override
    public Single<RowsAffectedResponse> changeGroup(int id, int order_num) {
        PutGroupRequest request = new PutGroupRequest();
        request.setId(id);
        request.setOrderNum(order_num);
        return mRetrofitClient.create(TaskService.class).changeGroup(mAuthHeader, request);
    }

    @Override
    public Single<RowsAffectedResponse> changeTask(int id, int order_num) {
        PutTaskRequest request = new PutTaskRequest();
        request.setOwnerUserId(order_num);
        return mRetrofitClient.create(TaskService.class).changeTask(mAuthHeader, id, request);
    }

    @Override
    public Single<RowsAffectedResponse> changeTask(int id, String descriptionOrDate, int type) {
        PutTaskRequest request = new PutTaskRequest();
        if (type == Constants.DESCRIPTION) {
            request.setTaskDescription(descriptionOrDate);
        } else {
            request.setTargetDate(descriptionOrDate);
        }
        return mRetrofitClient.create(TaskService.class).changeTask(mAuthHeader, id, request);
    }

    @Override
    public Single<RowsAffectedResponse> deleteTask(int id) {
        return mRetrofitClient.create(TaskService.class).deleteTask(mAuthHeader, id);
    }

    @Override
    public Single<RowsAffectedResponse> deleteGroup(int id) {
        return mRetrofitClient.create(TaskService.class).deleteGroup(mAuthHeader, id);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int taskType, String description) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int taskType, String description,
                                                 int orderNum) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setOrderNum(orderNum);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int taskType, String description,
                                                 String date) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setDate(date);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int meetingUserId, int taskType,
                                                 String description) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setOwnerUserId(meetingUserId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int meetingUserId, int taskType,
                                                 String description, int orderNum) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setOwnerUserId(meetingUserId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setOrderNum(orderNum);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int meetingUserId, int taskType,
                                                 String description, String date) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setOwnerUserId(meetingUserId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setDate(date);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int meetingUserId, int taskType,
                                                 String description, String date, int orderNum) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setOwnerUserId(meetingUserId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setDate(date);
        request.setOrderNum(orderNum);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertTaskResponse> insertTask(int groupId, int taskType, String description,
                                                 String date, int orderNum) {
        InsertTaskRequest request = new InsertTaskRequest();
        request.setGroupId(groupId);
        request.setTaskTypeId(taskType);
        request.setDescription(description);
        request.setDate(date);
        request.setOrderNum(orderNum);
        return mRetrofitClient.create(TaskService.class).insertTask(mAuthHeader, request);
    }

    @Override
    public Single<InsertGroupResponse> insertGroup(String name) {
        InsertGroupRequest request = new InsertGroupRequest();
        request.setGroupName(name);
        return mRetrofitClient.create(TaskService.class).insertGroup(mAuthHeader, request);
    }

    @Override
    public Single<InsertGroupResponse> insertGroup(String name, int orderNum) {
        InsertGroupRequest request = new InsertGroupRequest();
        request.setGroupName(name);
        request.setOrderNum(orderNum);
        return mRetrofitClient.create(TaskService.class).insertGroup(mAuthHeader, request);
    }

    @Override
    public Single<List<GetGroupResponse>> getGroupList() {
        return mRetrofitClient.create(TaskService.class).getGroupList(mAuthHeader);
    }

    @Override
    public Single<List<GetTaskResponse>> getTaskList() {
        return mRetrofitClient.create(TaskService.class).getTaskList(mAuthHeader);
    }

    @Override
    public Single<List<User>> getContact() {
        return mRetrofitClient.create(ContactsService.class).getContacts(mAuthHeader);
    }

    @Override
    public Single<User> getContact(int id) {
        return mRetrofitClient.create(ContactsService.class).getContact(mAuthHeader, id);
    }

    @Override
    public Single<List<User>> searchContactsByName(String name) {
        return mRetrofitClient.create(ContactsService.class).searchContactsByName(mAuthHeader, name);
    }

    @Override
    public Single<List<User>> searchContactsByPhone(String phone) {
        return mRetrofitClient.create(ContactsService.class).searchContactsByPhone(mAuthHeader, phone);
    }

    @Override
    public Single<AuthResponse> authenticateUser() {
        return mRetrofitClient.create(UserService.class).authenticateUser(mAuthHeader);
    }

    @Override
    public Single<UserResponse> getCurrentUser() {
        return mRetrofitClient.create(UserService.class).getCurrentUser(mAuthHeader);
    }

    @Override
    public Single<InsertContactResponse> insertContact(int contactId, String contactName) {
        InsertContactRequest request = new InsertContactRequest();
        request.setContactId(contactId);
        request.setContactName(contactName);
        return mRetrofitClient.create(ContactsService.class).insertContact(mAuthHeader, request);
    }

    @Override
    public Single<RowsAffectedResponse> deleteContact(int contactId) {
        return mRetrofitClient.create(ContactsService.class).deleteContact(mAuthHeader, contactId);
    }

    @Override
    public Single<UserPositionResponse> updateUserPosition(double latitude, double longitude) {
        UserPositionRequest request = new UserPositionRequest();
        request.setX(latitude);
        request.setY(longitude);
        return mRetrofitClient.create(UserService.class).updateUserPosition(mAuthHeader, request);
    }

    @Override
    public Single<List<ContactPosition>> getContactPositions() {
        return mRetrofitClient.create(ContactsService.class).getContactPositions(mAuthHeader);
    }

    @Override
    public Single<RegisterResponse> registerUser(User user) {
        return mRetrofitClient.create(UserService.class).registerUser(user);
    }

}
