package com.im.smarto.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.im.smarto.db.dao.UserDao;
import com.im.smarto.db.entities.User;

/**
 * Created by Anatoly Chernyshev on 28.02.2018.
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUsersDao();
}
