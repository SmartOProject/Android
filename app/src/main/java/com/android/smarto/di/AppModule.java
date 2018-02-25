package com.android.smarto.di;

import com.android.smarto.Constants;
import com.android.smarto.architecture.add_task.AddTaskActivity;
import com.android.smarto.architecture.add_task.types.common.CommonTaskFragment;
import com.android.smarto.architecture.add_task.types.goods.GoodsTaskFragment;
import com.android.smarto.architecture.add_task.types.MeetingTaskFragment;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.contacts.addcontact.AddContactActivity;
import com.android.smarto.architecture.fragments.ChatFragment;
import com.android.smarto.architecture.contacts.ContactFragment;
import com.android.smarto.architecture.fragments.HomeFragment;
import com.android.smarto.architecture.fragments.MapFragment;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.android.smarto.architecture.splash.SplashActivity;
import com.android.smarto.architecture.task.TaskFragment;
import com.android.smarto.data.TaskManager;
import com.android.smarto.data.DataManager;
import com.android.smarto.data.IDataManager;
import com.android.smarto.data.UsersManager;
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
    abstract INetworkHelper networkHelper(NetworkHelper networkHelper);

    @Singleton
    @Binds
    abstract IPreferenceHelper preferenceHelper(PreferenceHelper preferenceHelper);

    @Singleton
    @Binds
    abstract IDbHelper dbHelper(DbHelper dbHelper);

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
    public static TaskManager getTaskData(){
        return new TaskManager();
    }

    @Singleton
    @Provides
    public static UsersManager getUserData(){
        return new UsersManager();
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
    abstract HomeFragment homeFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ContactFragment contactFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChatFragment chatFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MapFragment mapFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract CommonTaskFragment commonTaskFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract GoodsTaskFragment goodsTaskFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract MeetingTaskFragment meetingTaskFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AddTaskActivity AddTaskActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AddContactActivity AddContactActivityInjector();

}
