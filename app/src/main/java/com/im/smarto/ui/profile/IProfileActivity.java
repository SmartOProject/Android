package com.im.smarto.ui.profile;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 19.03.2018.
 */

public interface IProfileActivity extends BaseView {
    void setProfileImage(String imgUrl);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setMobile(String phone);
}
