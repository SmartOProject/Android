package com.android.smarto.di;

import com.android.smarto.Constants;
import com.android.smarto.architecture.adding.AddTaskActivity;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.android.smarto.architecture.splash.SplashActivity;
import com.android.smarto.architecture.task.ITaskFragment;
import com.android.smarto.architecture.task.TaskFragment;
import com.android.smarto.architecture.task.TaskPresenter;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.data.DataManager;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.DbHelper;
import com.android.smarto.db.IDbHelper;
import com.android.smarto.prefs.IPreferenceHelper;
import com.android.smarto.prefs.PreferenceHelper;
import com.android.smarto.retrofit.INetworkHelper;
import com.android.smarto.retrofit.NetworkHelper;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anatoly Chernyshev on 12.02.2018.
 */

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class AppModule {

    @Singleton
    @Binds
    abstract IDataManager dataManager(DataManager dataManager);

    @Singleton
    @Binds
    abstract IDbHelper dbHelper(DbHelper dbHelper);

    @Singleton
    @Binds
    abstract INetworkHelper networkHelper(NetworkHelper networkHelper);

    @Singleton
    @Binds
    abstract IPreferenceHelper preferenceHelper(PreferenceHelper preferenceHelper);

    @Singleton
    @Provides
    public static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public static TaskData getTaskData(){
        return new TaskData();
    }

    @ActivityScope
    @ContributesAndroidInjector
    abstract NavigationActivity navigationActivityInjection();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SplashActivity splashActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RegisterActivity registerActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AuthActivity authActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract TaskFragment taskFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AddTaskActivity mAddTaskActivityInjector();

}
