package com.im.smarto.preferences;

import android.location.Location;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public interface IPreferenceHelper {

    String getToken();
    void saveToken(String token);

    Location getLocation();
    void updateLocation(Location location);

    boolean isRememberMe();
    void rememberMe(boolean isNeedToRemember);

}
