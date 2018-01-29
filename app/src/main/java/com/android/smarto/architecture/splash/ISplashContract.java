package com.android.smarto.architecture.splash;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public interface ISplashContract {

    interface ISplashPresenter {

        void isLoggedIn();

    }

    interface ISplashActivity {

        void openHomeActivity();
        void openAuthActivity();

    }



}
