package com.im.smarto.ui.splash;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public interface ISplashActivity extends BaseView{

        void openNavigationActivity(String token);
        void openAuthActivity();

}
