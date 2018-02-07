package com.android.smarto.architecture.navigation;

import com.android.smarto.architecture.base.BasePresenter;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class NavigationPresenter<V extends INavigationActivity> extends BasePresenter<V> {

    public void onCreate(){
        mView.setHomeFragment();
    }
    public void onHomeClicked(){
        mView.showHomeFragment();
    }
    public void onChatClicked(){
        mView.showChatFragment();
    }
    public void onContactClicked(){
        mView.showContactFragment();
    }
    public void onMapClicked(){
        mView.showMapFragment();
    }
    public void onTaskClicked(){
        mView.showTaskFragment();
    }
    public void onLogoutClicked(){
        mView.signOut();
    }

}
