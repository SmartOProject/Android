package com.im.smarto.ui.contacts.addcontact;

import android.text.TextUtils;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public class AddContactPresenter<V extends IAddContactActivity> extends BasePresenter<V> {

    public static final String TAG = AddContactPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private User mCurrentUserProfileDialog;

    @Inject
    public AddContactPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
        this.mCurrentUserProfileDialog = null;
    }

    public void onQueryTextChange(String text){

        if (TextUtils.isEmpty(text)) return;

        mView.hideContactHelper();
        if (!text.startsWith("+")) {
            mView.showProgressBar();
            mDataManager.networkHelper().searchContactsByName(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(users -> {
                        if (mView != null) {
                            Log.i(TAG, users.toString());
                            mDataManager.userManager().setSearchUserList(users);
                            mView.updateData(users);
                            mView.hideProgressBar();
                        }
                    });
        }
    }

    public void onQueryTextSubmit(String query){
        if (query.startsWith("+") && query.length() > 11) {
            mView.showProgressBar();
            mDataManager.networkHelper().searchContactsByPhone(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(users -> {
                        if (mView != null) {
                            Log.i(TAG, users.toString());
                            mDataManager.userManager().setSearchUserList(users);
                            mView.updateData(users);
                            mView.hideProgressBar();
                        }
                    });
        }
    }

    public void onViewResumed(){
        mView.setupRecyclerView();
    }

    public void onItemClicked(User user){
        mView.hideKeyboard();
        mCurrentUserProfileDialog = user;
        Log.i(TAG, user.getId() + " " + user.getName());
        mView.showProfileDialog(user.getId(),
                user.getImgUrl(),
                user.getName(),
                user.getPhone(),
                Constants.DIALOG_DELETE_ICON);
    }

    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        if (icon.equals(Constants.DIALOG_ADD_ICON)) {
            mView.changeDialogIcon();
            mDataManager.networkHelper()
                    .insertContact(mCurrentUserProfileDialog.getId(),
                            mCurrentUserProfileDialog.getName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(success -> {
                        Log.i(TAG, "New contact added!");
                        mView.showContactAddedSnackBar();
                        Observable.fromCallable(() -> {
                            mDataManager.userManager()
                                    .insertIntoContactMap(mCurrentUserProfileDialog.getId(),
                                            mCurrentUserProfileDialog.getName());
                            return true;
                        }).subscribeOn(Schedulers.io())
                                .subscribe();
                        return Single.just("Success");
                    })
                    .subscribe(s -> Log.i(TAG, s));
        } else {
            mView.changeDialogIcon();
            mDataManager.networkHelper()
                    .deleteContact(mCurrentUserProfileDialog.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(success -> {
                        Log.i(TAG, "New contact added!");
                        mView.showContactAddedSnackBar();
                        Observable.fromCallable(() -> {
                            mDataManager.userManager()
                                    .deleteFromContactMap(mCurrentUserProfileDialog.getId());
                            return true;
                        }).subscribeOn(Schedulers.io())
                                .subscribe();;
                        return Single.just("Success");
                    })
                    .subscribe(s -> Log.i(TAG, s));
        }
    }

}
