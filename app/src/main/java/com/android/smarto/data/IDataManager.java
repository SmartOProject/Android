package com.android.smarto.data;

import com.android.smarto.db.model.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface IDataManager {

    User getCurrentUser();
    void setCurrentUser(User user);
    TaskManager getTaskManager();
    Observable<List<User>> getFriends();
    String getUUID();
    void saveUUID(String id);
    Observable<List<User>> getUnfriends();
    Observable<List<User>> getSortedUnFriends(String name);
    User getUser(String query);
    List<User> getAllUsers();
    void addUser(User user);
    void removeFriend(User user);
    void addFriend(User user);
}
