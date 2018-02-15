package com.android.smarto.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.smarto.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

@Singleton
public class PreferenceHelper implements IPreferenceHelper {

    private SharedPreferences mPrefs;

    @Inject
    public PreferenceHelper(Context context) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Override
    public String getUUID() {
        return mPrefs.getString(Constants.USER_UUID, null);
    }

    @Override
    public void saveUUID(String uuid) {
        mPrefs.edit().putString(Constants.USER_UUID, uuid).apply();
    }


}
