package com.android.smarto.di.module;

import android.app.Application;

import com.android.smarto.Constants;
import com.android.smarto.prefs.ISharedPreferencesRepository;
import com.android.smarto.prefs.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    ISharedPreferencesRepository getPrefs(){
        return new SharedPreferencesRepository(mApplication);
    }

    @Provides
    @Singleton
    Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .build();
    }

}
