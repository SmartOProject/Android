package com.im.smarto.ui.navigation;

import com.im.smarto.ui.base.BaseView;
import com.im.smarto.db.entities.User;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface INavigationActivity extends BaseView {

    void showHomeFragment();
    void showChatFragment();
    void showTaskFragment();
    void showMapFragment();
    void showContactFragment();
    void initNavigationBar();

    void signOut();

    void openProfileActivity();

    void setupNavHeader(User currentUser);

    void showLogoutDialog();

    void showNetworkError();
}
