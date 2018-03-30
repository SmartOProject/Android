package com.im.smarto.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.im.smarto.db.converters.FriendsConverter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anatoly Chernyshev on 28.02.2018.
 */

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("phone")
    private String phone;
    @SerializedName("pwd")
    private String password;
    @SerializedName("trusted")
    private int trustId;

    @ColumnInfo(name = "img_url")
    @SerializedName("img_link")
    private String imgUrl;
    private double latitude;
    private double longitude;

    @ColumnInfo(name = "friends_id")
    @TypeConverters({FriendsConverter.class})
    private ArrayList<String> friendsId;

    @Ignore
    public User(int id, String name, String phone){
        this.id = id;
        this.firstName = name;
        this.phone = phone;
    }


    public User(int id, String firstName, String lastName, String phone, String imgUrl){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.imgUrl = imgUrl;
    }

    @Ignore
    public User(String phone, String firstName, String lastName, String password){
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Ignore
    public User(int id, String name, String phone, String password, String imgUrl,
                double latitude, double longitude){
        this.id = id;
        this.firstName = name;
        this.phone = phone;
        this.password = password;
        this.imgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public User(int id, String name, String phone, String password, String imgUrl,
                double latitude, double longitude, ArrayList<String> friendsIds){
        this.id = id;
        this.firstName = name;
        this.phone = phone;
        this.password = password;
        this.imgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.friendsId = friendsIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(ArrayList<String> friendsId) {
        this.friendsId = friendsId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTrustId() {
        return trustId;
    }

    public void setTrustId(int trustId) {
        this.trustId = trustId;
    }
}
