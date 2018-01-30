package com.android.smarto.di.component;

import com.android.smarto.architecture.splash.SplashActivity;
import com.android.smarto.di.module.PresenterModule;
import com.android.smarto.di.scope.PresenterScope;

import dagger.Subcomponent;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Subcomponent(modules = PresenterModule.class)
@PresenterScope
public interface PresenterComponent {

    void inject(SplashActivity splashActivity);

}
