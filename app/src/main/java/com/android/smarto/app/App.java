package com.android.smarto.app;

import android.app.Activity;
import android.app.Application;

import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.data.IDataManager;
import com.android.smarto.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    TaskData mTaskData;

    @Inject
    IDataManager mDataManager;

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

        mDataManager.getTaskList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> mTaskData.setList(e), Throwable::printStackTrace);

    }

    public static App get(){
        return instance;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
