package com.im.smarto.ui.profile;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 19.03.2018.
 */

public interface IProfileActivity extends BaseView {
    void showProfileImage();
    void showUsername(String username);
    void showMobile(String phone);
}
