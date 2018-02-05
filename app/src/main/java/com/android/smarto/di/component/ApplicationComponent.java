package com.android.smarto.di.component;

import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.home.HomeActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.android.smarto.architecture.splash.SplashActivity;
import com.android.smarto.di.module.ApplicationModule;
import com.android.smarto.utils.UtilityWrapper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(UtilityWrapper utilityWrapper);
    void inject(SplashActivity splashActivity);
    void inject(AuthActivity authActivity);
    void inject(RegisterActivity registerActivity);
    void inject(HomeActivity homeActivity);

}
