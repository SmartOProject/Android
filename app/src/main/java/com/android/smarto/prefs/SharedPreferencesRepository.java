package com.android.smarto.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.smarto.Constants;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class SharedPreferencesRepository implements ISharedPreferencesRepository {

    private SharedPreferences prefs;

    public SharedPreferencesRepository(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Override
    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.IS_LOGGED_IN, false);
    }

    @Override
    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean(Constants.IS_LOGGED_IN, loggedIn).commit();
    }


}
