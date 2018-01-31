package com.android.smarto.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public interface Test {

    @GET("url/")
    Call<Integer> testRequest();

}
