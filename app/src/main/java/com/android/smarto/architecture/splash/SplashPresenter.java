package com.android.smarto.architecture.splash;

import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.utils.UtilityWrapper;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashActivity> extends BasePresenter<V>{

    private static final String TAG = SplashPresenter.class.getSimpleName();

    private UtilityWrapper mUtilityWrapper;

    @Inject
    public SplashPresenter (UtilityWrapper utilityWrapper){
        mUtilityWrapper = utilityWrapper;
    }

    public void isLoggedIn(){

        Log.i(TAG, "isLoggedIn(): " + mUtilityWrapper.getSharedPreferencesRepository().isLoggedIn() + "");

        if (mUtilityWrapper.getSharedPreferencesRepository().isLoggedIn())
            mView.openHomeActivity();
        else
            mView.openAuthActivity();
    }

}
