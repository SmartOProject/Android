package com.im.smarto.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 14.03.2018.
 */

public class InsertTaskResponse {

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
