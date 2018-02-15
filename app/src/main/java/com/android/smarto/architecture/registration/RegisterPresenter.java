package com.android.smarto.architecture.registration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public class RegisterPresenter<V extends IRegisterActivity> extends BasePresenter<V>{

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private boolean mIsShowConfirmPasswordHelper = false;

    @Inject
    public RegisterPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onRegisterClicked(String email, String password, String confirmPassword,
                                  String firstName, String secondName, Uri profileImage){

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(firstName)
                || TextUtils.isEmpty(secondName)) {
            mView.showFieldEmptyError();
            return;
        }

        if (!isValidEmail(email)) {
            mView.showEmailError();
            return;
        }

        mDataManager.isEmailExist(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (!b) {
                        mView.showEmailExistError();
                        return;
                    }
                });

        if (!password.equals(confirmPassword)) {
            mView.showIncorrectConfirmPasswordError();
            return;
        }

        User user;
        if (profileImage == null){
        user = new User(email, password, firstName,
                                secondName, null);
        } else {
            user = new User(email, password, firstName,
                    secondName, profileImage.toString());
        }

        mDataManager.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> Log.i(TAG, "User added to db: " + user.getFirstName() + " "
                        + user.getSecondName()));

        mDataManager.setCurrentUser(user);
        mDataManager.saveUUID(user.getUniqueId());
        mView.openHomeActivity();

    }

    boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    mIsShowConfirmPasswordHelper = (Boolean) b;
                    mView.showConfirmPasswordHelper((Boolean) b);
                });
    }
}
