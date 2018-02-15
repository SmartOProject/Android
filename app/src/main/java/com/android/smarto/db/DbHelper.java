package com.android.smarto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.db.model.TaskList;
import com.android.smarto.db.model.User;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.android.smarto.Constants.Database.DATABASE_NAME;
import static com.android.smarto.Constants.Database.DATABASE_VERSION;
import static com.android.smarto.Constants.Database.KEY_EMAIL;
import static com.android.smarto.Constants.Database.KEY_FIRST_NAME;
import static com.android.smarto.Constants.Database.KEY_ID;
import static com.android.smarto.Constants.Database.KEY_IMAGE_PATH;
import static com.android.smarto.Constants.Database.KEY_LIST;
import static com.android.smarto.Constants.Database.KEY_PASSWORD;
import static com.android.smarto.Constants.Database.KEY_SECOND_NAME;
import static com.android.smarto.Constants.Database.TABLE_TASKS;
import static com.android.smarto.Constants.Database.TABLE_USERS;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class DbHelper extends SQLiteOpenHelper implements IDbHelper {

    public static final String TAG = DbHelper.class.getSimpleName();

    @Inject
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public boolean isEmailExist(String email) {

        Cursor cursor = getWritableDatabase().query(TABLE_USERS, null,
                null, null, null, null, null);

        Log.i(TAG, "IsEmailExist(" + email + "):");

        if (cursor.moveToFirst()){
            int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
            do{

                Log.i(TAG, "    Email: " + cursor.getString(emailIndex));

                if (email.equals(cursor.getString(emailIndex))) return true;

            } while(cursor.moveToNext());
        }

        return false;
    }

    @Override
    public boolean isCorrectUserInput(String email, String password) {

        Cursor cursor = getWritableDatabase().query(TABLE_USERS, null,
                null, null, null, null, null);

        Log.i(TAG, "IsCorrectUserInsert(" + email + ", " + password + "):");

        if (cursor.moveToFirst()){
            int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
            int passwordIndex = cursor.getColumnIndex(KEY_PASSWORD);
            do{

                Log.i(TAG,"    Email: " + cursor.getString(emailIndex) + "\n"
                        + "    Password: " + cursor.getString(passwordIndex));

                if (email.equals(cursor.getString(emailIndex))
                        && password.equals(cursor.getString(passwordIndex))){
                    return true;
                }

            } while(cursor.moveToNext());
        }

        return false;
    }

    @Override
    public User getUser(String query) {

        Cursor cursor = getWritableDatabase().query(TABLE_USERS, null,
                null, null, null, null, null);
        if (cursor.moveToFirst()){
            int uuidIndex = cursor.getColumnIndex(KEY_ID);
            int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
            int passwordIndex = cursor.getColumnIndex(KEY_PASSWORD);
            int firstNameIndex = cursor.getColumnIndex(KEY_FIRST_NAME);
            int secondNameIndex = cursor.getColumnIndex(KEY_SECOND_NAME);
            int profileImageIndex = cursor.getColumnIndex(KEY_IMAGE_PATH);

            do {

                Log.i(TAG, "GetUserFromEmailOrUUID(" + query + ")" + "\n"
                + "    UUID: " + cursor.getString(uuidIndex) + "\n"
                + "    Email: " + cursor.getString(emailIndex) + "\n"
                + "    Password: " + cursor.getString(passwordIndex) + "\n"
                + "    First name: " + cursor.getString(firstNameIndex) + "\n"
                + "    Second name: " + cursor.getString(secondNameIndex) + "\n"
                + "    Image path: " + cursor.getString(profileImageIndex));

                if (query.equals(cursor.getString(emailIndex))
                        || query.equals(cursor.getString(uuidIndex))){
                    return new User(cursor.getString(uuidIndex),
                            cursor.getString(emailIndex),
                            cursor.getString(passwordIndex),
                            cursor.getString(firstNameIndex),
                            cursor.getString(secondNameIndex),
                            cursor.getString(profileImageIndex));
                }

            } while (cursor.moveToNext());

        }

        return null;
    }

    @Override
    public List<TaskGroup> getTaskList() {

        Cursor cursor = getWritableDatabase().query(TABLE_TASKS, null,
                null, null, null, null, null);
        if (cursor.moveToFirst()){
            int taskIndex = cursor.getColumnIndex(KEY_LIST);

            do {
                Gson gson = new Gson();
                String list_json = cursor.getString(taskIndex);
                Log.i(TAG, list_json);
                List<TaskGroup> list = gson.fromJson(list_json, TaskList.class).getTaskList();
                return list;

            } while (cursor.moveToNext());

        }

        return null;

    }

    @Override
    public void addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, user.getUniqueId());
        contentValues.put(KEY_EMAIL, user.getEmail());
        contentValues.put(KEY_PASSWORD, user.getPassword());
        contentValues.put(KEY_FIRST_NAME, user.getFirstName());
        contentValues.put(KEY_SECOND_NAME, user.getSecondName());
        contentValues.put(KEY_IMAGE_PATH, user.getProfileImagePath());

        getWritableDatabase().insert(TABLE_USERS, null, contentValues);

        Log.i(TAG, "AddUser: " + "\n"
                + "    UUID: " + user.getUniqueId() + "\n"
                + "    Email: " + user.getEmail() + "\n"
                + "    Password: " + user.getPassword() + "\n"
                + "    First name: " + user.getFirstName() + "\n"
                + "    Second name: " + user.getSecondName() + "\n"
                + "    Image path: " + user.getProfileImagePath());

    }

    @Override
    public void addTaskList(List<TaskGroup> taskList) {

        getWritableDatabase().execSQL("delete from "+ TABLE_TASKS);

        TaskList list = new TaskList(taskList);
        Gson gson = new Gson();
        String list_json = gson.toJson(list);

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LIST, list_json);

        getWritableDatabase().insert(TABLE_TASKS, null, contentValues);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID
            + " text," + KEY_EMAIL + " text,"
            + KEY_PASSWORD + " text," + KEY_FIRST_NAME + " text,"
                + KEY_SECOND_NAME + " text," + KEY_IMAGE_PATH + " text" + ")");

        db.execSQL("create table " + TABLE_TASKS + "(" + KEY_LIST + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }

}
