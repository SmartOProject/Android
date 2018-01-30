package com.android.smarto.di.component;

import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.splash.ISplashContract;
import com.android.smarto.architecture.splash.SplashPresenter;
import com.android.smarto.di.module.PresenterModule;
import com.android.smarto.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SplashPresenter<ISplashContract.ISplashActivity> splashPresenter);
    void inject(AuthActivity authActivity);

    PresenterComponent getActivityComponent(PresenterModule presenterModule);

}
