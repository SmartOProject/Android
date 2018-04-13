package com.im.smarto.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 25.02.2018.
 */

public class MapPresenter<V extends IMapFragment> extends BasePresenter<V> {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private boolean mPermissionsGranted;

    @Inject
    public MapPresenter(IDataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void onMapReady() {
        if (mView != null) {
            mCompositeDisposable.add(
                    mDataManager
                            .networkHelper()
                            .getContactPositions()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(positions -> {
                                if (positions.size() != 0)
                                    mView.setupMap(positions);
                            }, error -> Log.i(TAG, "Download positions failed!")));

            if (mDataManager.prefHelper().getLocation() != null)
                mView.showCurrentUserPosition(mDataManager.prefHelper().getLocation());
        }
    }

    public void onMyLocationClicked() {
        Log.i(TAG, "onMyLocationClicked() " + mPermissionsGranted);
        if (!mPermissionsGranted) return;
        mView.showLocationSettingsDialog();
    }

    @SuppressLint("MissingPermission")
    public void startLocationCheck(){
        mDataManager.locationManager()
                .fusedLocationProviderClient()
                .requestLocationUpdates(new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(500)
                        .setFastestInterval(100)
                        .setSmallestDisplacement(0), new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location: locationResult.getLocations()){
                            Log.i(TAG, location.getLatitude() + " | " + location.getLongitude());
                            mView.showCurrentPosition(location);
                            mDataManager.locationManager().fusedLocationProviderClient()
                                    .removeLocationUpdates(this);
                            mDataManager.prefHelper().updateLocation(location);
                            mCompositeDisposable.add(
                                    mDataManager
                                            .networkHelper()
                                            .updateUserPosition(location.getLatitude(),
                                                    location.getLongitude())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(success -> Log.i(TAG, "Coordinates updated!"),
                                                    error -> Log.i(TAG, "Update error!")));
                        }
                    }
                }, Looper.myLooper());
    }

    public void onCreate() {
        mView.checkLocationPermissions();
        mView.initMap();
    }

    public void onPermissionsGranted() {
        mPermissionsGranted = true;
    }

    public void onPermissionsDenied() {
        mPermissionsGranted = false;
        mView.showPermissionsDeniedMessage();
    }

}
