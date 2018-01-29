package com.android.smarto.prefs;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public interface ISharedPreferencesRepository {

    boolean isLoggedIn();
    void setLoggedIn(boolean loggedIn);

}
