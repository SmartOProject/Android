package com.im.smarto.ui.splash;

import android.util.Log;

import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashActivity> extends BasePresenter<V> {

    private static final String TAG = SplashPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    public SplashPresenter (IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onCreate() {
        String token = mDataManager.prefHelper().getToken();
        Log.i(TAG, "Token: " + token);
        if (token != null) mView.openNavigationActivity(token);
            else mView.openAuthActivity();
    }
}
