package com.im.smarto.ui.registration;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;
import com.im.smarto.utils.HttpErrorUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import retrofit2.HttpException;

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
                                  String firstName, String lastName){

        String phone = convertPhone(mobileNumber);
        String creditionals = phone + ":" + password;
        String basic = "Basic " + Base64.encodeToString(creditionals.getBytes(), Base64.NO_WRAP);
        Log.i(TAG, "Basic: " + basic);

        if (TextUtils.isEmpty(mobileNumber) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(firstName)
                    || TextUtils.isEmpty(lastName)) {
            mView.showFieldEmptyError();
            return;
        }

        if (mobileNumber.length() < 12) {
            mView.showMobileInputError();
            return;
        }

        if (!password.equals(confirmPassword)) {
            mView.showIncorrectConfirmPasswordError();
            return;
        }

        User user = new User(phone, firstName, lastName, password);
        mDataManager.networkHelper().registerUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i(TAG, "User register success!");
                    mDataManager.userManager().setCurrentUser(user);
                    mDataManager.networkHelper().setHeader(basic);
                    mView.openNavigationActivity();
                        }, error -> {
                    String[] splittedError = HttpErrorUtils.getHttpErrorBody(error).split(" ");
                    if (splittedError[3].equals("(SMARTO.APP_USERS_PHONE_UNQ)"))
                        mView.showPhoneExistDialog();
                    Log.i(TAG, HttpErrorUtils.getHttpErrorBody(error));
                });

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

    String convertPhone(String phone){
        String phoneNumber = phone;
        phoneNumber = phoneNumber.replace(" ","");
        phoneNumber = phoneNumber.replace(")","");
        phoneNumber = phoneNumber.replace("(","");
        phoneNumber = phoneNumber.replace("-","");
        Log.i(TAG, phoneNumber);
        return phoneNumber;
    }

}
