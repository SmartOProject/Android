package com.im.smarto.ui.registration;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public interface IRegisterActivity extends BaseView {

        void showConfirmPasswordHelper(boolean show);
        void openNavigationActivity();
        void showFieldEmptyError();
        void showMobileInputError();
        void showIncorrectConfirmPasswordError();
        void showUserExistError();

}
