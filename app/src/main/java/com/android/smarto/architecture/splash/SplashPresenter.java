package com.android.smarto.architecture.splash;

import android.content.Context;

import com.android.smarto.prefs.SharedPreferencesRepository;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SplashPresenter implements ISplashContract.ISplashPresenter {

    private SharedPreferencesRepository sharedPreferencesRepository;

    private ISplashContract.ISplashActivity splashActivity;

    public SplashPresenter(ISplashContract.ISplashActivity splashActivity, Context context) {
        this.splashActivity = splashActivity;
        sharedPreferencesRepository = new SharedPreferencesRepository(context);
    }

    @Override
    public void isLoggedIn() {

        if (sharedPreferencesRepository.isLoggedIn())
            splashActivity.openHomeActivity();
        else
            splashActivity.openAuthActivity();
    }
}
