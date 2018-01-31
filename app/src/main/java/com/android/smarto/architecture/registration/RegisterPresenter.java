package com.android.smarto.architecture.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.Presenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public class RegisterPresenter<V extends IRegisterContract.IRegisterActivity> extends Presenter<V>
        implements IRegisterContract.IRegisterPresenter<V> {

    private boolean mIsShowConfirmPasswordHelper = false;

    @Override
    public void onRegisterClick(String login, String password, String confirmPassword) {
        if (Constants.TEST_USERNAME.equals(login) || password.length() < 4
                || !password.equals(confirmPassword) || mIsShowConfirmPasswordHelper)
            getView().showError();
        else {
            getPrefs().setLoggedIn(true);
            getView().openHomeActivity();
        }
    }

    @Override
    public void onProfileImageClick(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        
        activity.startActivityForResult(intent, Constants.RESULT_LOAD_IMAGE);
    }

    @Override
    public void setObservables(Observable passwordObs, Observable confirmPasswordObs) {
        Observable.combineLatest(passwordObs, confirmPasswordObs,
                (password, confirmPassword) -> !password.toString()
                        .equals(confirmPassword.toString()))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    mIsShowConfirmPasswordHelper = (Boolean) b;
                    getView().showConfirmPasswordHelper((Boolean) b);
                });
    }
}
