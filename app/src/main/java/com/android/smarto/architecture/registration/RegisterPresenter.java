package com.android.smarto.architecture.registration;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.utils.UtilityWrapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public class RegisterPresenter<V extends IRegisterActivity> extends BasePresenter<V>{

    private UtilityWrapper mUtilityWrapper;

    private boolean mIsShowConfirmPasswordHelper = false;

    @Inject
    public RegisterPresenter(UtilityWrapper utilityWrapper){
        mUtilityWrapper = utilityWrapper;
    }

    public void onRegisterClick(String login, String password, String confirmPassword){
        if (Constants.TEST_USERNAME.equals(login) || password.length() < 4
                || !password.equals(confirmPassword) || mIsShowConfirmPasswordHelper)
            mView.showError();
        else {
            mUtilityWrapper.getSharedPreferencesRepository().setLoggedIn(true);
            mView.openHomeActivity();
        }
    }

    public void onProfileImageClick(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        
        activity.startActivityForResult(intent, Constants.RESULT_LOAD_IMAGE);
    }

    public void setObservables(Observable passwordObs, Observable confirmPasswordObs){
        Observable.combineLatest(passwordObs, confirmPasswordObs,
                (password, confirmPassword) -> !password.toString()
                        .equals(confirmPassword.toString()))
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    mIsShowConfirmPasswordHelper = (Boolean) b;
                    mView.showConfirmPasswordHelper((Boolean) b);
                });
    }
}
