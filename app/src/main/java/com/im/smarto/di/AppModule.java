package com.im.smarto.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.im.smarto.Constants;
import com.im.smarto.db.DbHelper;
import com.im.smarto.db.IDbHelper;
import com.im.smarto.ui.add_task.AddTaskActivity;
import com.im.smarto.ui.add_task.types.common.CommonTaskFragment;
import com.im.smarto.ui.add_task.types.goods.GoodsTaskFragment;
import com.im.smarto.ui.add_task.types.meeting.MeetingTaskFragment;
import com.im.smarto.ui.authentication.AuthActivity;
import com.im.smarto.ui.contacts.addcontact.AddContactActivity;
import com.im.smarto.ui.fragments.ChatFragment;
import com.im.smarto.ui.contacts.ContactFragment;
import com.im.smarto.ui.group.GroupActivity;
import com.im.smarto.ui.home.HomeFragment;
import com.im.smarto.ui.map.MapFragment;
import com.im.smarto.ui.navigation.NavigationActivity;
import com.im.smarto.ui.profile.ProfileActivity;
import com.im.smarto.ui.registration.RegisterActivity;
import com.im.smarto.ui.splash.SplashActivity;
import com.im.smarto.ui.task.TaskFragment;
import com.im.smarto.data.TaskManager;
import com.im.smarto.data.DataManager;
import com.im.smarto.data.IDataManager;
import com.im.smarto.data.UsersManager;
import com.im.smarto.preferences.IPreferenceHelper;
import com.im.smarto.preferences.PreferenceHelper;
import com.im.smarto.network.INetworkHelper;
import com.im.smarto.network.NetworkHelper;
import com.im.smarto.db.AppDatabase;
import com.im.smarto.db.dao.UserDao;
import com.google.android.gms.location.LocationRequest;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
    @Provides
    public static IDbHelper dbHelper(UserDao userDao){
        return new DbHelper(userDao);
    }

    @Singleton
    @Provides
    public static LocationRequest getLocationRequest(){
        return new LocationRequest();
    }

    @Singleton
    @Provides
    public static AppDatabase getDb(Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "SmartO.db").build();
    }

    @Singleton @Provides
    public static UserDao provideUserDao(AppDatabase myDatabase){
        return myDatabase.getUsersDao();
    }

    @Singleton
    @Provides
    public static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    public static UsersManager getUserData(Context context){
        return new UsersManager(context);
    }

    @ActivityScope
    @ContributesAndroidInjector
    abstract NavigationActivity navigationActivityInjection();

    @ActivityScope
    @ContributesAndroidInjector
    abstract SplashActivity splashActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract GroupActivity groupActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract RegisterActivity registerActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract AuthActivity authActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ProfileActivity profileActivityInjector();

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
