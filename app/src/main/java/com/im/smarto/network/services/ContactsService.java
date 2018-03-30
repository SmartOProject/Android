package com.im.smarto.network.services;

import com.im.smarto.db.entities.User;
import com.im.smarto.network.models.ContactPosition;
import com.im.smarto.network.models.ContactRequest;
import com.im.smarto.network.models.ContactResponse;
import com.im.smarto.network.models.InsertContactRequest;
import com.im.smarto.network.models.InsertContactResponse;
import com.im.smarto.network.models.RowsAffectedResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anatoly Chernyshev on 02.03.2018.
 */

public interface ContactsService {

    /**
     * API: BASE_URL + contact
     */

    @GET("contact")
    Single<List<User>> getContacts(@Header("Authorization") String basic);

    @GET("contact/{id}")
    Single<User> getContact(@Header("Authorization") String basic,
                            @Path("id") int id);

    @GET("contact_user_position")
    Single<List<ContactPosition>> getContactPositions(@Header("Authorization") String basic);

    @POST("contact")
    Single<InsertContactResponse> insertContact(@Header("Authorization") String basic,
                                                @Body InsertContactRequest insertContactRequest);

    @PUT("contact/{id}")
    Single<ContactResponse> updateContact(@Header("Authorization") String basic,
                                          @Path("id") int id,
                                          @Body ContactRequest request);

    @DELETE("contact/{id}")
    Single<RowsAffectedResponse> deleteContact(@Header("Authorization") String basic,
                                               @Path("id") int id);


    /**
     * API: BASE_URL + search_contact
     */

    @GET("search_contact")
    Single<List<User>> searchContactsByName(@Header("Authorization") String basic, @Query("name") String name);

    @GET("search_contact")
    Single<List<User>> searchContactsByPhone(@Header("Authorization") String basic, @Query("phone") String phone);

}
