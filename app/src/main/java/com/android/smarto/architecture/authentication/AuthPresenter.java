package com.android.smarto.architecture.authentication;

import android.text.TextUtils;
import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public class AuthPresenter<V extends IAuthActivity> extends BasePresenter<V>{

    private static final String TAG = AuthPresenter.class.getSimpleName();

    private IDataManager mDataManager;
    private String mMobile;

    @Inject
    public AuthPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onLoginClicked(String mobileNumber, String password){

        Log.i(TAG, "onLoginClicked()");

        mMobile = reformatNumber(mobileNumber);

        if (TextUtils.isEmpty(mMobile) || TextUtils.isEmpty(password)) {
            mView.showEmptyMobileNumberError();
            return;
        }

        User user = mDataManager.getUser(mMobile);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                mDataManager.saveUUID(user.getUniqueId());
                mDataManager.setCurrentUser(user);
                mView.openHomeActivity();
            } else mView.showIncorrectLoginDataError();
        } else mView.showIncorrectLoginDataError();
    }

    private String reformatNumber(String mobile) {
        String format = mobile;
        format = format.replace(" ", "");
        format = format.trim();
        format = format.replace("(", "");
        format = format.replace(")", "");
        return format;
    }

}
