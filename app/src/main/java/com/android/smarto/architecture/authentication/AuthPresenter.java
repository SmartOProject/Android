package com.android.smarto.architecture.authentication;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.Presenter;
import com.android.smarto.data.UserData;
import com.android.smarto.prefs.ISharedPreferencesRepository;
import com.android.smarto.prefs.SharedPreferencesRepository;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter <V extends IAuthContract.IAuthActivity> extends Presenter<V> implements IAuthContract.IAuthPresenter <V> {

    @Inject
    ISharedPreferencesRepository mSharedPreferencesRepository;

    @Override
    public void confirmUserData() {
        UserData userData = getView().getUserData();
        if (TextUtils.isEmpty(userData.getLogin()) || TextUtils.isEmpty(userData.getPassword()))
            getView().showEmptyLoginDataError();
        else
            if (userData.getLogin().equals(Constants.TEST_USERNAME) && userData.getPassword().equals(Constants.TEST_PASSWORD)) {
                mSharedPreferencesRepository.setLoggedIn(true);
                getView().openHomeActivity();
            }
            else
                getView().showIncorrectLoginDataError();

    }

}
