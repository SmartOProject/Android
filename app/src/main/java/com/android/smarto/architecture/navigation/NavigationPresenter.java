package com.android.smarto.architecture.navigation;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class NavigationPresenter<V extends INavigationActivity> extends BasePresenter<V> {

    private IDataManager mDataManager;

    @Inject
    public NavigationPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onCreate() { mView.showHomeFragment();}
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
        mDataManager.saveUUID(null);
        mDataManager.setCurrentUser(null);
        mView.signOut();
    }

}
