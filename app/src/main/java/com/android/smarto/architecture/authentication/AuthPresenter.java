package com.android.smarto.architecture.authentication;

import android.content.Context;
import android.text.TextUtils;

import com.android.smarto.Constants;
import com.android.smarto.data.UserData;
import com.android.smarto.prefs.SharedPreferencesRepository;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter implements IAuthContract.IAuthPresenter {

    private IAuthContract.IAuthActivity authActivity;
    private SharedPreferencesRepository sharedPreferencesRepository;

    public AuthPresenter(IAuthContract.IAuthActivity authActivity, Context context) {
        this.authActivity = authActivity;

        sharedPreferencesRepository = new SharedPreferencesRepository(context);
    }

    @Override
    public void confirmUserData() {
        UserData userData = authActivity.getUserData();
        if (TextUtils.isEmpty(userData.getLogin()) || TextUtils.isEmpty(userData.getPassword()))
            authActivity.showEmptyLoginDataError();
        else
            if (userData.getLogin().equals(Constants.TEST_USERNAME) && userData.getPassword().equals(Constants.TEST_PASSWORD)) {
                sharedPreferencesRepository.setLoggedIn(true);
                authActivity.openHomeActivity();
            }
            else
                authActivity.showIncorrectLoginDataError();

    }
}
