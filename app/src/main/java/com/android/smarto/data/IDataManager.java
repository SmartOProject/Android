package com.android.smarto.data;

import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.db.model.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface IDataManager {

    User getCurrentUser();
    void setCurrentUser(User currentUser);

    String getUUID();
    void saveUUID(String uuid);

    Observable<User> getUser(String query);
    Observable<Boolean> isCorrectUserInput(String email, String password);
    Observable<Boolean> isEmailExist(String email);
    Observable<Void> addUser(User user);
    Observable<Void> addTaskList(List<TaskGroup> taskList);
    Observable<List<TaskGroup>> getTaskList();




}
