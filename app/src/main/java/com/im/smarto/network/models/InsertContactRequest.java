package com.im.smarto.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anatoly Chernyshev on 13.03.2018.
 */

public class InsertContactRequest {

    @SerializedName("contact_user_id")
    @Expose
    private int contactId;
    @SerializedName("contact_name")
    @Expose
    private String contactName;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
