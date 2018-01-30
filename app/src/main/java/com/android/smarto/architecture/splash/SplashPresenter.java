package com.android.smarto.architecture.splash;

import com.android.smarto.app.App;
import com.android.smarto.architecture.base.Presenter;
import com.android.smarto.prefs.ISharedPreferencesRepository;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter<V extends ISplashContract.ISplashActivity> extends Presenter<V> implements ISplashContract.ISplashPresenter<V> {

    @Inject
    ISharedPreferencesRepository mSharedPreferencesRepository;

    @Override
    public void isLoggedIn() {

        if (mSharedPreferencesRepository.isLoggedIn())
            getView().openHomeActivity();
        else
            getView().openAuthActivity();
    }

    @Override
    public void onStart() {
        App.get().getApplicationComponent().inject(this);
    }
}
