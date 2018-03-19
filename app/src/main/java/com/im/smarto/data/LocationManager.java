package com.im.smarto.data;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 13.03.2018.
 */

@Singleton
public class LocationManager {

    private static final String TAG = LocationManager.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Context mContext;

    @Inject
    public LocationManager(Context context) {
        mContext = context;
        init();
    }

    public void init() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    public FusedLocationProviderClient fusedLocationProviderClient(){
        return mFusedLocationProviderClient;
    }

}
