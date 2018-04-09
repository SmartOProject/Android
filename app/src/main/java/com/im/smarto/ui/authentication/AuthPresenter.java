package com.im.smarto.ui.authentication;

import android.util.Base64;
import android.util.Log;

import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter<V extends IAuthActivity> extends BasePresenter<V>{

    private static final String TAG = AuthPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    AuthPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    void onLoginClicked(String phone, String password, boolean isNeedToRemember) {

        mView.showProgressBar();
        String creditionals = phone + ":" + password;
        String basic = "Basic " + Base64.encodeToString(creditionals.getBytes(), Base64.NO_WRAP);

        mDataManager
                .networkHelper()
                .setHeader(basic);

        Log.i(TAG, "Basic: " + basic);
        mCompositeDisposable.add(mDataManager.networkHelper().authenticateUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i(TAG, "Authentication success! " + "Token - " + response.getToken());
                    if (isNeedToRemember) mDataManager.prefHelper().saveToken(response.getToken());
                    mView.openNavigationActivity(response.getToken());
                    mView.hideProgressBar();
                }, error -> {
                    Log.i(TAG, "Error!" + "\n" + error.getMessage());
                    mView.showAuthenticationFailedError();
                    mView.hideProgressBar();
                }));
    }

    public void onSignUpButtonClicked() {
        mView.openRegisterActivity();
    }
}
