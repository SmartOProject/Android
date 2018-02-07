package com.android.smarto.di.module;

import android.app.Application;

import com.android.smarto.Constants;
import com.android.smarto.architecture.navigation.INavigationActivity;
import com.android.smarto.architecture.navigation.NavigationPresenter;
import com.android.smarto.prefs.ISharedPreferencesRepository;
import com.android.smarto.prefs.SharedPreferencesRepository;
import com.android.smarto.utils.UtilityWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Module
@Singleton
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    UtilityWrapper provideUtils(){
        return new UtilityWrapper(providePrefs(), provideRetrofit());
    }

    @Provides
    @Singleton
    ISharedPreferencesRepository providePrefs(){
        return new SharedPreferencesRepository(mApplication);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NavigationPresenter<INavigationActivity> provideNavigationPresenter(){
        return new NavigationPresenter<>();
    }

}
