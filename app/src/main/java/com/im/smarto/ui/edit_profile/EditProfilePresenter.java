package com.im.smarto.ui.edit_profile;

import com.im.smarto.Constants;
import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 28.03.2018.
 */

public class EditProfilePresenter<V extends IEditProfileActivity> extends BasePresenter<V> {

    private IDataManager mDataManager;
    private String mType;

    @Inject
    public EditProfilePresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreate(String type) {
        this.mType = type;
        switch (mType) {
            case Constants.FIRST_NAME:
                mView.setupLayoutForEditFirstName();
                break;
            case Constants.LAST_NAME:
                mView.setupLayoutForEditLastName();
                break;
            case Constants.PHONE:
                mView.setupLayoutForEditPhone();
                break;

        }
    }

    public void onSaveClicked(String s) {
        switch (mType) {
            case Constants.FIRST_NAME:
                mDataManager.userManager().getCurrentUser().setFirstName(s);
                mView.finish();
                break;
            case Constants.LAST_NAME:
                mDataManager.userManager().getCurrentUser().setLastName(s);
                mView.finish();
                break;
            case Constants.PHONE:
                mDataManager.userManager().getCurrentUser().setPhone(s);
                mView.finish();
                break;
        }
    }
}
