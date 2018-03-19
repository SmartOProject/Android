package com.im.smarto.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.im.smarto.db.entities.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Anatoly Chernyshev on 28.02.2018.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    Flowable<List<User>> getAll();

    @Query("SELECT * FROM users WHERE id = :query OR phone = :query")
    Single<User> getUser(String query);

    @Query("SELECT friends_id FROM users WHERE id = :ownerId")
    Flowable<List<String>> getFriendsIds(String ownerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

}
