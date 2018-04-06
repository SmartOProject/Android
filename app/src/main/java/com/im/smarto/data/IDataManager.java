package com.im.smarto.data;

import com.im.smarto.db.IDbHelper;
import com.im.smarto.network.INetworkHelper;
import com.im.smarto.preferences.IPreferenceHelper;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public interface IDataManager {

    IPreferenceHelper prefHelper();
    INetworkHelper networkHelper();
    UsersManager userManager();
    TaskManager taskManager();
    LocationManager locationManager();

}
