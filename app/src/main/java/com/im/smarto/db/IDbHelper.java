package com.im.smarto.db;

import com.im.smarto.db.entities.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Anatoly Chernyshev on 28.02.2018.
 */

public interface IDbHelper {

    Single<User> getUser(String query);
    Completable insertUsers(User... users);
    Completable updateUser(User... users);
    Flowable<List<User>> getAll();
    Flowable<List<String>> getFriendsIds(String ownerId);

}
