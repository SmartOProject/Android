package com.im.smarto.ui.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class NavigationPresenter<V extends INavigationActivity> extends BasePresenter<V> {

    public static final String TAG = NavigationPresenter.class.getSimpleName();

    private IDataManager mDataManager;
    private Context mContext;
    private String token;

    @Inject
    public NavigationPresenter(IDataManager dataManager, Context context) {
        this.mDataManager = dataManager;
        this.mContext = context;
    }

    public void onCreate() {

        String token = mDataManager.prefHelper().getToken();

        Log.i(TAG, "onCreate() " + token);

        if (token != null) {
            String authHeader = "Bearer " + token;
            mDataManager
                    .networkHelper()
                    .setHeader(authHeader);
        }

        if (mDataManager.userManager().getCurrentUser() == null) {
            mDataManager
                    .networkHelper()
                    .getCurrentUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        Log.i(TAG, response.toString() + "\n" + response.getUserPhone());
                        User user = new User(response.getId(), response.getFirstName(),
                                response.getLastName(),
                                response.getUserPhone(),
                                response.getImgLink());
                        mDataManager.userManager().setCurrentUser(user);
                        mDataManager.prefHelper().cacheCurrentUser(user);
                        mView.setupNavHeader(user);
                    }, error -> {
                        Log.i(TAG, "getCurrentUser() " + error.getMessage());

                        if (error.getMessage().equals(Constants.NETWORK_ERROR))
                            mView.showNetworkError();

                        mDataManager
                                .userManager()
                                .setCurrentUser(mDataManager.prefHelper().getCachedUser());
                        mView.setupNavHeader(mDataManager.userManager().getCurrentUser());
                    });
        } else {

        }

        mDataManager.networkHelper().getContact()
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .flatMap(users -> Single.fromCallable(() -> {
                    mDataManager.userManager().updateContactsMap(users);
                    return true;
                }))
                .subscribe(success -> Log.i(TAG, "yes"),
                        error -> Log.i(TAG, error.getMessage()));

        mView.showHomeFragment();
        mView.initNavigationBar();
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

        mView.showLogoutDialog();


    }

    public void onProfileClicked() {
        mView.openProfileActivity();
    }

    public void onResume() {
        if (mDataManager.userManager().getCurrentUser() != null)
            mView.setupNavHeader(mDataManager.userManager().getCurrentUser());
    }

    public void onPositiveButtonClicked() {
        mDataManager.userManager().setCurrentUser(null);
        mDataManager.prefHelper().saveToken(null);
        mDataManager.taskManager().mData.clear();
        mView.signOut();
    }

    public void onNegativeButtonClicked() {

    }
}
