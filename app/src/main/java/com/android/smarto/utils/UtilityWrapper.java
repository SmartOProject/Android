package com.android.smarto.utils;

import com.android.smarto.prefs.ISharedPreferencesRepository;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public class UtilityWrapper {

    private ISharedPreferencesRepository mSharedPreferencesRepository;

    private Retrofit mRetrofit;

    @Inject
    public UtilityWrapper(ISharedPreferencesRepository sharedPreferencesRepository,
                          Retrofit retrofit){
        mSharedPreferencesRepository = sharedPreferencesRepository;
        mRetrofit = retrofit;
    }

    public ISharedPreferencesRepository getSharedPreferencesRepository(){
        return mSharedPreferencesRepository;
    }

    public Retrofit getRetrofitClient(){
        return mRetrofit;
    }

}
