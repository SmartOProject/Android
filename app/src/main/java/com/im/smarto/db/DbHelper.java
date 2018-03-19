package com.im.smarto.db;

import com.im.smarto.db.dao.UserDao;
import com.im.smarto.db.entities.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Anatoly Chernyshev on 28.02.2018.
 */

public class DbHelper implements IDbHelper {

    private UserDao mUserDao;

    @Inject
    public DbHelper(UserDao userDao){
        this.mUserDao = userDao;
    }

    @Override
    public Single<User> getUser(String query) {
        return mUserDao.getUser(query);
    }

    @Override
    public Completable insertUsers(User... users) {
        return Completable.fromAction(() -> mUserDao.insert(users));
    }

    @Override
    public Completable updateUser(User... users) {
        return Completable.fromAction(() -> mUserDao.update(users));
    }

    @Override
    public Flowable<List<User>> getAll() {
        return mUserDao.getAll();
    }

    @Override
    public Flowable<List<String>> getFriendsIds(String ownerId) {
        return mUserDao.getFriendsIds(ownerId);
    }
}
