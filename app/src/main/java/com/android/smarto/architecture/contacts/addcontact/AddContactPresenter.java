package com.android.smarto.architecture.contacts.addcontact;

import android.view.View;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public class AddContactPresenter<V extends IAddContactActivity> extends BasePresenter<V> {

    public static final String TAG = AddContactPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    public AddContactPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onQueryTextChange(String newText){
        mView.showProgressBar();
        mDataManager.getSortedUnFriends(newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mView.setupRecyclerView(e);
                    mView.hideProgressBar();
                });
    }

    public void onQueryTextSubmit(String query){
        mView.showProgressBar();
        mDataManager.getSortedUnFriends(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mView.setupRecyclerView(e);
                    mView.hideProgressBar();
                });
    }

    public void onViewResumed(){
        mView.showProgressBar();
        mDataManager.getUnfriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mView.setupRecyclerView(e);
                    mView.hideProgressBar();
                });
    }

    public void onItemClicked(View v, int position, String id){
        User user = mDataManager.getUser(id);
        mView.showProfileDialog(user.profileImagePath, user.getName(),
                user.getMobileNumber());
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
