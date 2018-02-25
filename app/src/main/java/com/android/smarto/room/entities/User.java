//package com.android.smarto.room.entities;
//
//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
//import com.android.smarto.Constants;
//import com.google.gson.annotations.SerializedName;
//
///**
// * Created by Anatoly Chernyshev on 21.02.2018.
// */
//
//@Entity(tableName = Constants.Database.USER_TABLE_NAME)
//public class User {
//
//    @SerializedName("user_id")
//    @PrimaryKey
//    private String id;
//
//    @SerializedName("email")
//    @ColumnInfo(name = "email")
//    private String email;
//
//    @SerializedName("password")
//    @ColumnInfo(name = "password")
//    private String password;
//
//    @SerializedName("first_name")
//    @ColumnInfo(name = "first_name")
//    private String firstName;
//
//    @SerializedName("last_name")
//    @ColumnInfo(name = "last_name")
//    private String lastName;
//
//    @SerializedName("img_path")
//    @ColumnInfo(name = "img_path")
//    private String imagePath;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
//}
