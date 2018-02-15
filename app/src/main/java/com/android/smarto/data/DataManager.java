package com.android.smarto.data;

import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.db.IDbHelper;
import com.android.smarto.db.model.User;
import com.android.smarto.prefs.IPreferenceHelper;
import com.android.smarto.retrofit.INetworkHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class DataManager implements IDataManager {

    @Inject
    TaskData mTaskData;

    private final IPreferenceHelper mPreferenceHelper;
    private final INetworkHelper mNetworkHelper;
    private final IDbHelper mDbHelper;

    private User currentUser;

    @Inject
    public DataManager(IPreferenceHelper preferenceHelper,
                       INetworkHelper networkHelper, IDbHelper dbHelper){
        this.mPreferenceHelper = preferenceHelper;
        this.mNetworkHelper = networkHelper;
        this.mDbHelper = dbHelper;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String getUUID() {
        return mPreferenceHelper.getUUID();
    }

    @Override
    public void saveUUID(String uuid) {
        mPreferenceHelper.saveUUID(uuid);
    }

    @Override
    public Observable<User> getUser(String query) {
        return Observable.fromCallable(() -> mDbHelper.getUser(query));
    }

    @Override
    public Observable<Boolean> isCorrectUserInput(String email, String password) {
        return Observable.fromCallable(() -> mDbHelper.isCorrectUserInput(email, password));
    }

    @Override
    public Observable<Boolean> isEmailExist(String email) {
        return Observable.fromCallable(() -> mDbHelper.isEmailExist(email));
    }

    @Override
    public Observable<Void> addUser(User user) {
        return Observable.create(e -> mDbHelper.addUser(user));
    }

    @Override
    public Observable<Void> addTaskList(List<TaskGroup> taskList) {
        return Observable.create(e -> mDbHelper.addTaskList(taskList));
    }

    @Override
    public Observable<List<TaskGroup>> getTaskList() {
        return Observable.fromCallable(()-> mDbHelper.getTaskList());
    }
}
