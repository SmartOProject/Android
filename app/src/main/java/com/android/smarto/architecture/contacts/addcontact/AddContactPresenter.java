package com.android.smarto.architecture.contacts.addcontact;

import android.util.Log;
import android.view.View;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
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
        mDataManager.getUnfriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(users -> users)
                .filter(user -> user.getName().toLowerCase().contains(newText) || user.getMobileNumber().contains(newText))
                .toList()
                .subscribe(users -> {
                    if (mView != null) {
                        mView.setupRecyclerView(users);
                        mView.hideProgressBar();
                    }
                });

    }

    public void onQueryTextSubmit(String query){
        mView.showProgressBar();
        mDataManager.getUnfriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(users -> users)
                .filter(user -> user.getName().toLowerCase().contains(query) || user.getMobileNumber().contains(query))
                .toList()
                .subscribe(users -> {
                    if (mView != null) {
                        mView.setupRecyclerView(users);
                        mView.hideProgressBar();
                    }
                });
    }

    public void onViewResumed(){
        mView.showProgressBar();
        mDataManager.getUnfriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (mView != null) {
                        mView.setupRecyclerView(users);
                        mView.hideProgressBar();
                    }
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
