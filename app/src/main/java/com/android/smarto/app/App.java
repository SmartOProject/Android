package com.android.smarto.app;

import android.app.Application;

import com.android.smarto.di.component.ApplicationComponent;
import com.android.smarto.di.component.DaggerApplicationComponent;
import com.android.smarto.di.module.ApplicationModule;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public class App extends Application{

    private ApplicationComponent mApplicationComponent;

    private static App instance;

    @Override
    public void onCreate(){
        super.onCreate();

        instance = this;

        mApplicationComponent = getApplicationComponent();
    }

    public static App get(){
        return instance;
    }

    public ApplicationComponent getApplicationComponent(){

        if (mApplicationComponent == null) mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        return mApplicationComponent;

    }

}
