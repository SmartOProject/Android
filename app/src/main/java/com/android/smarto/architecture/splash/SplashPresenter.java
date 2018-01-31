package com.android.smarto.architecture.splash;

import com.android.smarto.architecture.base.Presenter;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashContract.ISplashActivity> extends Presenter<V> implements ISplashContract.ISplashPresenter<V> {

    @Override
    public void isLoggedIn() {

        if (getPrefs().isLoggedIn())
            getView().openHomeActivity();
        else
            getView().openAuthActivity();
    }

}
