package com.android.smarto.architecture.splash;

import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashActivity> extends BasePresenter<V>{

    private static final String TAG = SplashPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    public SplashPresenter (IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onCreate(){

        Log.i(TAG, "onCreate()");

        String uuid = mDataManager.getUUID();

        Log.i(TAG, "onCreate() " + uuid);

        if (uuid != null) {
            User user = mDataManager.getUser(uuid);
            mDataManager.setCurrentUser(user);
            mView.openNavigationActivity();
        }
        else mView.openAuthActivity();
    }

}
