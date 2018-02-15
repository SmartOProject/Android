package com.android.smarto.retrofit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class NetworkHelper implements INetworkHelper {

    private final Retrofit mRetrofitClient;

    @Inject
    public NetworkHelper(Retrofit retrofitClient){
          this.mRetrofitClient = retrofitClient;
    }

    @Override
    public Retrofit getRetrofitClient() {
        return mRetrofitClient;
    }
}
