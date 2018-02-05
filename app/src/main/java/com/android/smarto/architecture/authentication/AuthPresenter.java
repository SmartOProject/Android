package com.android.smarto.architecture.authentication;

import android.text.TextUtils;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.UserData;
import com.android.smarto.utils.UtilityWrapper;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter<V extends IAuthActivity> extends BasePresenter<V>{

    private UtilityWrapper mUtilityWrapper;

    @Inject
    public AuthPresenter(UtilityWrapper utilityWrapper){
        mUtilityWrapper = utilityWrapper;
    }

    public void confirmUserData(){
        UserData userData = mView.getUserData();
        if (TextUtils.isEmpty(userData.getLogin()) || TextUtils.isEmpty(userData.getPassword()))
            mView.showEmptyLoginDataError();
        else
            if (userData.getLogin().equals(Constants.TEST_USERNAME) && userData.getPassword().equals(Constants.TEST_PASSWORD)) {
                mUtilityWrapper.getSharedPreferencesRepository().setLoggedIn(true);
                mView.openHomeActivity();
            }
            else
                mView.showIncorrectLoginDataError();

    }

}
