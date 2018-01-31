package com.android.smarto.architecture.authentication;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.data.UserData;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public interface IAuthContract {

    interface IAuthActivity extends BaseView {

        void openHomeActivity();
        void showEmptyLoginDataError();
        void showIncorrectLoginDataError();
        UserData getUserData();

    }

    interface IAuthPresenter <V extends BaseView> extends BasePresenter <V> {

        void confirmUserData();

    }

    interface IAuthModel {



    }

}
