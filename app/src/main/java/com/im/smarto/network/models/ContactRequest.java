package com.im.smarto.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 29.03.2018.
 */

public class ContactRequest {

    @SerializedName("trusted")
    private int isTrust;

    public int getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(int isTrust) {
        this.isTrust = isTrust;
    }
}
