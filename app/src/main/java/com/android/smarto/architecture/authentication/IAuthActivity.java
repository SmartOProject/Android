package com.android.smarto.architecture.authentication;

import com.android.smarto.architecture.ILoading;
import com.android.smarto.architecture.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public interface IAuthActivity extends BaseView, ILoading{

        void openHomeActivity();
        void showEmptyMobileNumberError();
        void showIncorrectLoginDataError();

}
