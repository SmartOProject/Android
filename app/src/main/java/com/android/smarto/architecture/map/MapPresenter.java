package com.android.smarto.architecture.map;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 25.02.2018.
 */

public class MapPresenter<V extends IMapFragment> extends BasePresenter<V> {

    private IDataManager mDataManager;

    @Inject
    public MapPresenter(IDataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void onMapReady(){
        mDataManager.getFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mView.setupMap(e, mDataManager.getCurrentUser());
                });
    }

}
