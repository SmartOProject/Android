package com.android.smarto.architecture.navigation;

import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.db.model.User;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public interface INavigationActivity extends BaseView {

    void showHomeFragment();
    void showChatFragment();
    void showTaskFragment();
    void showMapFragment();
    void showContactFragment();
    void initNavigationBar(User user);
    void onItemUndoAction(long id);

    void signOut();

}
