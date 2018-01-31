package com.android.smarto.architecture.splash;

import android.util.Log;

import com.android.smarto.architecture.base.Presenter;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashContract.ISplashActivity> extends Presenter<V> implements ISplashContract.ISplashPresenter<V> {

    private static final String TAG = SplashPresenter.class.getSimpleName();

    @Override
    public void isLoggedIn() {

        Log.i(TAG, "isLoggedIn(): " + getPrefs().isLoggedIn() + "");

        if (getPrefs().isLoggedIn())
            getView().openHomeActivity();
        else
            getView().openAuthActivity();
    }

}
