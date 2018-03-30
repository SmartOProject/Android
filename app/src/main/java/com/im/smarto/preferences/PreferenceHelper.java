package com.im.smarto.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.im.smarto.Constants;
import com.google.gson.Gson;
import com.im.smarto.db.entities.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

@Singleton
public class PreferenceHelper implements IPreferenceHelper {

    private SharedPreferences mPrefs;
    private Gson mGson;

    @Inject
    public PreferenceHelper(Context context) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mGson = new Gson();

    }

    @Override
    public String getToken() {
        return mPrefs.getString(Constants.USER_TOKEN, null);
    }

    @Override
    public void saveToken(String token) {
        mPrefs.edit().putString(Constants.USER_TOKEN, token).apply();
    }

    @Override
    public Location getLocation() {
        String location = mPrefs.getString(Constants.CURRENT_LOCATION, "");
        return mGson.fromJson(location, Location.class);
    }

    @Override
    public void updateLocation(Location location) {
        String currentLocation = mGson.toJson(location);
        mPrefs.edit().putString(Constants.CURRENT_LOCATION, currentLocation).apply();
    }

    @Override
    public boolean isRememberMe() {
        return mPrefs.getBoolean(Constants.REMEMBER_USER, false);
    }

    @Override
    public void rememberMe(boolean isNeedToRemember) {
        mPrefs.edit().putBoolean(Constants.REMEMBER_USER, isNeedToRemember).apply();
    }

    @Override
    public void cacheCurrentUser(User user) {
        mPrefs.edit().putString(Constants.CURRENT_USER_FIRST_NAME, user.getFirstName()).apply();
        mPrefs.edit().putString(Constants.CURRENT_USER_LAST_NAME, user.getLastName()).apply();
        mPrefs.edit().putString(Constants.CURRENT_USER_PHONE, user.getPhone()).apply();
        mPrefs.edit().putInt(Constants.CURRENT_USER_ID, user.getId()).apply();
        mPrefs.edit().putString(Constants.CURRENT_USER_IMG_LINK, user.getImgUrl()).apply();
    }

    @Override
    public User getCachedUser() {
        int id = mPrefs.getInt(Constants.CURRENT_USER_ID, 0);
        String firstName = mPrefs.getString(Constants.CURRENT_USER_FIRST_NAME, "");
        String lastName = mPrefs.getString(Constants.CURRENT_USER_LAST_NAME, "");
        String phone = mPrefs.getString(Constants.CURRENT_USER_PHONE, "");
        String imgLink = mPrefs.getString(Constants.CURRENT_USER_IMG_LINK, "");
        return new User(id, firstName, lastName, phone, imgLink);
    }


}
