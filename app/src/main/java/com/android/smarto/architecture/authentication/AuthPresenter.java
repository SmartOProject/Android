package com.android.smarto.architecture.authentication;

import android.text.TextUtils;
import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;

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
    public AuthPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onLoginClicked(String email, String password){

        Log.i(TAG, "onLoginClicked()");

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            mView.showEmptyLoginDataError();
            return;
        }

        mDataManager.isCorrectUserInput(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    if (e) {
                        mDataManager.getUser(email)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(u -> {
                                    mDataManager.setCurrentUser(u);
                                    mDataManager.saveUUID(u.getUniqueId());
                                    mView.openHomeActivity();
                                });
                    } else {
                        mView.showIncorrectLoginDataError();
                        return;
                    }
                });
    }

}
