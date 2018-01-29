package com.android.smarto.architecture.authentication;

import com.android.smarto.architecture.IProgressBar;
import com.android.smarto.data.UserData;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public interface IAuthContract extends IProgressBar {

    interface IAuthActivity {

        void openHomeActivity();
        void showEmptyLoginDataError();
        void showIncorrectLoginDataError();
        UserData getUserData();

    }

    interface IAuthPresenter {

        void confirmUserData();

    }

    interface IAuthModel {



    }

}
