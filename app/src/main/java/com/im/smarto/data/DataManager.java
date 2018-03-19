package com.im.smarto.data;

import com.im.smarto.preferences.IPreferenceHelper;
import com.im.smarto.network.INetworkHelper;
import com.im.smarto.db.IDbHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class DataManager implements IDataManager {

    public static final String TAG = DataManager.class.getSimpleName();

    private final IDbHelper mDbHelper;
    private final INetworkHelper mNetworkHelper;
    private final IPreferenceHelper mPreferenceHelper;
    private final UsersManager mUsersManager;
    private final TaskManager mTaskManager;
    private final LocationManager mLocationManager;

    @Inject
    public DataManager(IDbHelper dbHelper, INetworkHelper networkHelper,
                       LocationManager locationManager, IPreferenceHelper preferenceHelper,
                       UsersManager usersManager, TaskManager taskManager) {
        this.mDbHelper = dbHelper;
        this.mNetworkHelper = networkHelper;
        this.mPreferenceHelper = preferenceHelper;
        this.mUsersManager = usersManager;
        this.mTaskManager = taskManager;
        this.mLocationManager = locationManager;
    }

    @Override
    public IDbHelper dbHelper() {
        return mDbHelper;
    }

    @Override
    public IPreferenceHelper prefHelper() {
        return mPreferenceHelper;
    }

    @Override
    public INetworkHelper networkHelper() {
        return mNetworkHelper;
    }

    @Override
    public UsersManager userManager() {
        return mUsersManager;
    }

    @Override
    public TaskManager taskManager() {
        return mTaskManager;
    }

    @Override
    public LocationManager locationManager() {
        return mLocationManager;
    }
}
