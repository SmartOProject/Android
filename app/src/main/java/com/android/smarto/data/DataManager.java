package com.android.smarto.data;

import com.android.smarto.db.IDbHelper;
import com.android.smarto.db.model.User;
import com.android.smarto.prefs.IPreferenceHelper;
import com.android.smarto.retrofit.INetworkHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class DataManager implements IDataManager {

    public static final String TAG = DataManager.class.getSimpleName();

    private final IPreferenceHelper mPreferenceHelper;
    private final INetworkHelper mNetworkHelper;
    private final IDbHelper mDbHelper;

    private TaskManager mTaskManager;

    @Inject
    public DataManager(IPreferenceHelper preferenceHelper,
                       INetworkHelper networkHelper, IDbHelper dbHelper){
        this.mPreferenceHelper = preferenceHelper;
        this.mNetworkHelper = networkHelper;
        this.mDbHelper = dbHelper;
    }

    @Override
    public String getUUID() {
        return mPreferenceHelper.getUUID();
    }

    @Override
    public void saveUUID(String uuid) {
        mPreferenceHelper.saveUUID(uuid);
    }

    public Retrofit getRetrofitClient() {
        return null;
    }

    @Override
    public User getCurrentUser() {
        return mDbHelper.getCurrentUser();
    }

    @Override
    public void setCurrentUser(User user) {
        mDbHelper.setCurrentUser(user);
    }

    @Override
    public TaskManager getTaskManager() {
        if (mTaskManager == null) mTaskManager = mDbHelper.getTaskManager();
        return mTaskManager;
    }

    @Override
    public Observable<List<User>> getFriends() {
        return Observable.create(e -> {
            e.onNext(mDbHelper.getFriendList());
            e.onComplete();
        });
    }

    @Override
    public Observable<List<User>> getUnfriends() {
        return Observable.create(e ->{
            e.onNext(mDbHelper.getUnfriends());
            e.onComplete();
        });
    }

    @Override
    public Observable<List<User>> getSortedUnFriends(String name) {
        return Observable.create(e -> {
            e.onNext(mDbHelper.getSortedUnFriends(name));
            e.onComplete();
        });
    }

    @Override
    public User getUser(String query) {
        return mDbHelper.getUser(query);
    }

    @Override
    public List<User> getAllUsers() {
        return mDbHelper.getAllUsers();
    }

    @Override
    public void addUser(User user) {
        mDbHelper.addUser(user);
    }

    @Override
    public void removeFriend(User user) {
        mDbHelper.removeFriend(user);
    }

    @Override
    public void addFriend(User user) {
        mDbHelper.addFriend(user);
    }

}
