package com.im.smarto.network.services;

import com.im.smarto.db.entities.User;
import com.im.smarto.network.models.AuthResponse;
import com.im.smarto.network.models.RegisterResponse;
import com.im.smarto.network.models.RowsAffectedResponse;
import com.im.smarto.network.models.UpdateUserRequest;
import com.im.smarto.network.models.UserPositionRequest;
import com.im.smarto.network.models.UserPositionResponse;
import com.im.smarto.network.models.UserResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Anatoly Chernyshev on 05.03.2018.
 */

public interface UserService {

    @GET("auth")
    Single<AuthResponse> authenticateUser(@Header("Authorization") String basic);

    @GET("user/0")
    Single<UserResponse> getCurrentUser(@Header("Authorization") String token);

    @POST("register_user")
    Single<RegisterResponse> registerUser(@Body User user);

    @PUT("user")
    Single<RowsAffectedResponse> updateUser(@Header("Authorization") String auth,
                                            @Body UpdateUserRequest request);

    @PUT("user_position")
    Single<UserPositionResponse> updateUserPosition(@Header("Authorization") String basic,
                                                    @Body UserPositionRequest request);

}
