package com.android.smarto.architecture.splash;

import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;

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

        if (uuid != null) {
            mDataManager.getUser(uuid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        mDataManager.setCurrentUser(e);
                        mView.openNavigationActivity();
                    });
        }
        else
            mView.openAuthActivity();
    }

}
