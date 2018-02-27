package com.android.smarto.app;

import android.app.Activity;
import android.app.Application;

import com.android.smarto.di.DaggerAppComponent;
import com.google.firebase.FirebaseApp;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private static App instance;

    @Override
    public void onCreate(){
        super.onCreate();

        instance = this;

        DaggerAppComponent
                .builder()
                .context(this)
                .build()
                .inject(this);

    }

    public static App get(){
        return instance;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
