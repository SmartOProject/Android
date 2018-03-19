package com.im.smarto.ui.map;

import android.util.Log;

import com.im.smarto.network.models.ContactPosition;
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

    @Inject
    public MapPresenter(IDataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void onMapReady(){
        if (mView != null) {
            mDataManager.networkHelper().getContactPositions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(positions -> {
                        if (positions.size() != 0) {
                            if (mDataManager.prefHelper().getLocation() == null) {
                                mView.setupMap(positions, null);
                            } else {
                                ContactPosition myPosition = new ContactPosition();
                                myPosition.setFirstName("You");
                                myPosition.setLatitude(mDataManager.prefHelper().getLocation().getLatitude());
                                myPosition.setLongitude(mDataManager.prefHelper().getLocation().getLongitude());

                                positions.add(myPosition);
                                mView.setupMap(positions, myPosition);
                            }
                        }
                    }, error -> Log.i(TAG, "Download positions failed!"));
        }
    }

}
