package com.android.smarto.architecture.authentication;

import android.text.TextUtils;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.Presenter;
import com.android.smarto.data.UserData;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter <V extends IAuthContract.IAuthActivity> extends Presenter<V> implements IAuthContract.IAuthPresenter <V> {

    @Override
    public void confirmUserData() {
        UserData userData = getView().getUserData();
        if (TextUtils.isEmpty(userData.getLogin()) || TextUtils.isEmpty(userData.getPassword()))
            getView().showEmptyLoginDataError();
        else
            if (userData.getLogin().equals(Constants.TEST_USERNAME) && userData.getPassword().equals(Constants.TEST_PASSWORD)) {
                getPrefs().setLoggedIn(true);
                getView().openHomeActivity();
            }
            else
                getView().showIncorrectLoginDataError();

    }

}
