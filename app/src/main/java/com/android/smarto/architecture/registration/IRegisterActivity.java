package com.android.smarto.architecture.registration;

import com.android.smarto.architecture.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 31.01.2018.
 */

public interface IRegisterActivity extends BaseView{

        void showConfirmPasswordHelper(boolean show);
        void openHomeActivity();
        void showFieldEmptyError();
        void showEmailError();
        void showIncorrectConfirmPasswordError();
        void showEmailExistError();

}
