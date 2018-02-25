package com.android.smarto.architecture.contacts;

import android.view.View;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public class ContactsPresenter<V extends IContactsFragment> extends BasePresenter<V> {

    public static final String TAG = ContactsPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    public ContactsPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onPermissionsGranted(){
        mView.showProgressBar();
        mDataManager.getFriends()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    if (mView != null) {
                        mView.setupRecyclerView(e);
                        mView.hideProgressBar();
                    }
                });
    }

    public void onFABCLicked(){
        mView.startAddActivity();
    }

    public void onItemClicked(View v, int position, String id){
        mView.showProgressBar();
        User user = mDataManager.getUser(id);
        mView.showProfileDialog(user.profileImagePath, user.getName(),
                user.getMobileNumber(), R.drawable.ic_delete_black_24dp);
        mView.hideProgressBar();
    }

    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        User user = mDataManager.getUser(mobileNumber);
        if (icon.equals(Constants.DIALOG_DELETE_ICON)) {
            mView.changeDialogIcon();
            mDataManager.removeFriend(user);
            mView.updateList();
        } else {
            mView.changeDialogIcon();
            mDataManager.addFriend(user);
            mView.updateList();
        }
    }

}
