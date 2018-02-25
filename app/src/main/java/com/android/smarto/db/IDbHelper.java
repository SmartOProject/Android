package com.android.smarto.db;

import com.android.smarto.data.TaskManager;
import com.android.smarto.db.model.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface IDbHelper {

   TaskManager getTaskManager();
   User getUser(String query);
   List<User> getAllUsers();
   void addUser(User user);
   User getCurrentUser();
   void setCurrentUser(User user);
   void removeFriend(User user);
   void addFriend(User user);

   List<User> getFriendList();
   List<User> getUnfriends();
   List<User> getSortedUnFriends(String name);

}
