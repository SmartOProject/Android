package com.android.smarto.architecture.registration;

import android.app.Activity;
import android.content.Context;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public interface IRegisterContract {

    interface IRegisterActivity extends BaseView{

        void showConfirmPasswordHelper(boolean show);
        void openHomeActivity();
        void showError();

    }

    interface IRegisterPresenter<V extends BaseView> extends BasePresenter<V>{

        void onRegisterClick(String login, String password, String confirmPassword);
        void onProfileImageClick(Activity activity);
        void setObservables(Observable password, Observable confirmPassword);

    }

}
