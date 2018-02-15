package com.android.smarto.db.model;

import java.util.UUID;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class User {

    private String uniqueId;
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String profileImagePath;

    public User(String email, String password, String firstName,
                String secondName, String profileImagePath) {
        this.uniqueId = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.profileImagePath = profileImagePath;
    }

    public User(String UUID, String email, String password, String firstName,
                String secondName, String profileImagePath) {
        this.uniqueId = UUID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.profileImagePath = profileImagePath;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
