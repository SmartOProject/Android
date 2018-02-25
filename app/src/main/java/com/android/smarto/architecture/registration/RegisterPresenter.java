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

    public void onRegisterClicked(String mobileNumber, String password, String confirmPassword,
                                  String firstName, String lastName, Uri profileImage){

        if (TextUtils.isEmpty(mobileNumber) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(firstName)
                    || TextUtils.isEmpty(lastName)) {
            mView.showFieldEmptyError();
            return;
        }

        if (!validMobilePhone(mobileNumber)) {
            mView.showMobileInputError();
            return;
        }

        if (mDataManager.getUser(mobileNumber) != null){
            mView.showUserExistError();
            return;
        }

        if (!password.equals(confirmPassword)) {
            mView.showIncorrectConfirmPasswordError();
            return;
        }

        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (profileImage == null) user.setProfileImagePath("");
            else user.setProfileImagePath(profileImage.toString());

        mDataManager.addUser(user);

        mDataManager.setCurrentUser(user);
        mDataManager.saveUUID(user.getUniqueId());
        mView.openHomeActivity();

    }

    public boolean validMobilePhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
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
