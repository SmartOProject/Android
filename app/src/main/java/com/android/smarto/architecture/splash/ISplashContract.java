package com.android.smarto.architecture.splash;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public interface ISplashContract {

    interface ISplashPresenter<V extends ISplashActivity> extends BasePresenter<V>{

        void isLoggedIn();

    }

    interface ISplashActivity extends BaseView{

        void openHomeActivity();
        void openAuthActivity();

    }



}
