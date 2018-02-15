package com.android.smarto.db;

import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.db.model.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface IDbHelper {

    void addUser(User user);
    void addTaskList(List<TaskGroup> taskList);

    boolean isEmailExist(String email);
    boolean isCorrectUserInput(String email, String password);
    User getUser(String query);
    List<TaskGroup> getTaskList();


}
