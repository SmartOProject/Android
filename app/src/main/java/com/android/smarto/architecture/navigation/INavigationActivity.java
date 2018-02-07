package com.android.smarto.architecture.navigation;

import com.android.smarto.architecture.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface INavigationActivity extends BaseView {

    void setHomeFragment();

    void showHomeFragment();
    void showChatFragment();
    void showTaskFragment();
    void showMapFragment();
    void showContactFragment();

    void signOut();

}
