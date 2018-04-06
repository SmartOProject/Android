package com.im.smarto.dialog.contact;

import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 04.04.2018.
 */

public interface IContactDialog extends BaseView {

    void initContactViews(User contactById);

    void changeTrustState();

    void dismissDialog();

    void showNetworkError();

    void setupDeleteAndTrustButton();

    void showUserAddedToast();
}
