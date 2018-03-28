package com.im.smarto.ui.profile;

import com.im.smarto.data.IDataManager;
import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 19.03.2018.
 */

public class ProfilePresenter<V extends IProfileActivity> extends BasePresenter<V> {

    private IDataManager mDataManager;

    @Inject
    public ProfilePresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onCreate() {
        User currentUser = mDataManager.userManager().getCurrentUser();

        if (currentUser.getImgUrl() != null) mView.setProfileImage(currentUser.getImgUrl());

        mView.setFirstName(currentUser.getFirstName());
        mView.setLastName(currentUser.getLastName());
        mView.setMobile(currentUser.getPhone());
    }
}
