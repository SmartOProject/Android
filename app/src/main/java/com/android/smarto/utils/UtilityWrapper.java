package com.android.smarto.utils;

import com.android.smarto.prefs.ISharedPreferencesRepository;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public class UtilityWrapper {

    @Inject
    ISharedPreferencesRepository mSharedPreferencesRepository;

    @Inject
    Retrofit mRetrofit;

    public ISharedPreferencesRepository getmSharedPreferencesRepository(){
        return mSharedPreferencesRepository;
    }

    public Retrofit getRetrofitClient(){
        return mRetrofit;
    }

}
