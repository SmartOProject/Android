package com.im.smarto.ui.edit_profile;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 28.03.2018.
 */

public interface IEditProfileActivity extends BaseView {
    void setupLayoutForEditFirstName();

    void setupLayoutForEditLastName();

    void setupLayoutForEditPhone();

    void finish();
}
