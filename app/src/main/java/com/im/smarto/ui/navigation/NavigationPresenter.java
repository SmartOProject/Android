package com.im.smarto.ui.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class NavigationPresenter<V extends INavigationActivity> extends BasePresenter<V> {

    public static final String TAG = NavigationPresenter.class.getSimpleName();

    private IDataManager mDataManager;
    private Context mContext;

    @Inject
    public NavigationPresenter(IDataManager dataManager, Context context) {
        this.mDataManager = dataManager;
        this.mContext = context;
    }

    public void onCreate(String token) {
        mView.showHomeFragment();

        if (mDataManager.userManager().getCurrentUser() == null) {
            String bearer = "Bearer " + token;
            mDataManager.networkHelper().getCurrentUser(bearer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        Log.i(TAG, response.toString() + "\n" + response.getUserPhone());
                        User user = new User(response.getId(), response.getFirstName(),
                                response.getLastName(),
                                response.getUserPhone(),
                                response.getImgLink());
                        mView.initNavigationBar(user);
                        mDataManager.userManager().setCurrentUser(user);
                    }, error -> Log.i(TAG, error.getMessage()));
        } else mView.initNavigationBar(mDataManager.userManager().getCurrentUser());
    }

    public void onHomeClicked() {
        mView.showHomeFragment();
    }

    public void onChatClicked() {
        mView.showChatFragment();
    }

    public void onContactClicked() {
        mView.showContactFragment();
    }

    public void onMapClicked() {
        mView.showMapFragment();
    }

    public void onTaskClicked() {
        mView.showTaskFragment();
    }

    public void onLogoutClicked() {
        mDataManager.userManager().setCurrentUser(null);
        mDataManager.prefHelper().saveToken(null);
        mDataManager.taskManager().mData.clear();
        mView.signOut();
    }

    public void onItemUndoActionClicked() {
        mView.onItemUndoAction(mDataManager.taskManager().undoLastRemoval());
    }

    public void onProfileClicked() {
        mView.openProfileActivity();
    }
}
