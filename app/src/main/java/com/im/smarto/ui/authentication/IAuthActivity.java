package com.im.smarto.ui.authentication;

import com.im.smarto.ui.ILoading;
import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 25.01.18.
 */

public interface IAuthActivity extends BaseView, ILoading{

        void openNavigationActivity(String token);
        void openRegisterActivity();
        void showIncorrectPhoneNumberError();
        void showEmptyFieldsError();
        void showAuthenticationFailedError();

}
