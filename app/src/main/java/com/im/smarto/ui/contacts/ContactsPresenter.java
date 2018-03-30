package com.im.smarto.ui.contacts;

import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public class ContactsPresenter<V extends IContactsFragment> extends BasePresenter<V> {

    public static final String TAG = ContactsPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private User mCurrentUserProfileDialog;

    @Inject
    public ContactsPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
        this.mCurrentUserProfileDialog = null;
    }

    public void onPermissionsGranted(){
        updateList();
    }

    public void onFABCLicked(){
        mView.startAddActivity();
    }

    public void onItemClicked(User user){
        mCurrentUserProfileDialog = user;
        mView.showProfileDialog(user.getImgUrl(),
                user.getName(),
                user.getPhone(), user.getTrustId());
    }

    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        mView.changeDialogIcon();
        mDataManager.networkHelper()
                .deleteContact(mCurrentUserProfileDialog.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(rowsAffectedResponse -> {
                    mView.dismissDialog();
                    mView.showProgressBar();
                    Observable.fromCallable(() -> {
                        mDataManager.userManager()
                                .deleteFromContactMap(mCurrentUserProfileDialog.getId());
                        return true;
                    }).subscribeOn(Schedulers.io())
                            .subscribe();
                    return mDataManager.networkHelper().getContact()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap(users -> {
                                logUsers(users);
                                mView.updateData(users);
                                return Single.just("Success");
                            });
                })
                .subscribe(s -> mView.hideProgressBar());
    }

    public void updateList() {
        mView.showProgressBar();
        mDataManager.networkHelper().getContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(users -> {
                    logUsers(users);
                    mView.updateData(users);
                    return Single.fromCallable(() -> {
                        mDataManager.userManager().updateContactsMap(users);
                        return true;
                    });
                })
                .subscribe(s -> mView.hideProgressBar(),
                        error -> {
                            Log.i(TAG, error.getMessage());
                            if (error.getMessage().equals(Constants.NETWORK_ERROR))
                                mView.showNetworkError();
                            mView.hideProgressBar();
                        });
    }

    private void logUsers(List<User> users) {
        for (User user: users) {
            Log.i(TAG, "Phone: " + user.getPhone() + "\n"
            + "Name: " + user.getName() + "\n");
        }
    }

    public void onCreate() {
        mView.setupRecyclerView();
    }

    public void onCheckBoxClicked(boolean checked) {
        Log.i(TAG, checked + "");

        int trustId;
        if (checked) trustId = 1;
        else trustId = 0;

        mDataManager
                .networkHelper()
                .updateContact(mCurrentUserProfileDialog.getId(), trustId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rows -> {
                            Log.i(TAG, rows.toString());
                            updateList();
                        },
                        error -> {
                            if (error.getMessage().equals(Constants.NETWORK_ERROR))
                                mView.showNetworkError();
                        });
    }

}
