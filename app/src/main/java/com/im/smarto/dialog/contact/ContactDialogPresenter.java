package com.im.smarto.dialog.contact;

import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.data.IDataManager;
import com.im.smarto.db.entities.User;
import com.im.smarto.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 04.04.2018.
 */

public class ContactDialogPresenter<V extends IContactDialog> extends BasePresenter<V> {

    private static final String TAG = ContactDialogPresenter.class.getSimpleName();

    private IDataManager mDataManager;
    private User mCurrentContact;
    private boolean mIsDialogFromContacts;

    @Inject
    public ContactDialogPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreateView(int currentContactId, boolean isFromContacts) {

        mIsDialogFromContacts = isFromContacts;

        if (!isFromContacts) {
            mView.setupDeleteAndTrustButton();
            mCurrentContact = mDataManager.userManager().getUserById(currentContactId);
        } else mCurrentContact = mDataManager.userManager().getContactById(currentContactId);

        mView.initContactViews(mCurrentContact);
    }

    public void onTrustClicked(int currentTrustId) {
        if (currentTrustId == 0) {
            mDataManager
                    .userManager()
                    .getContactById(mCurrentContact.getId())
                    .setTrustId(1);

            mCompositeDisposable.add(mDataManager
                    .networkHelper()
                    .updateContact(mCurrentContact.getId(), 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rows -> Log.i(TAG, rows.toString()),
                            error -> {
                                if (error.getMessage().equals(Constants.NETWORK_ERROR))
                                    mView.showNetworkError();
                            }));
        } else {
            mDataManager
                    .userManager()
                    .getContactById(mCurrentContact.getId())
                    .setTrustId(0);

            mCompositeDisposable.add(mDataManager
                    .networkHelper()
                    .updateContact(mCurrentContact.getId(), 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rows -> Log.i(TAG, rows.toString()),
                            error -> {
                                if (error.getMessage().equals(Constants.NETWORK_ERROR))
                                    mView.showNetworkError();
                            }));
        }
        mView.changeTrustState();
    }

    public void onDeleteClicked() {

        if (mIsDialogFromContacts) {

            mDataManager
                    .userManager()
                    .deleteContact(mCurrentContact);

            mCompositeDisposable.add(mDataManager
                    .networkHelper()
                    .deleteContact(mCurrentContact.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rows -> mView.dismissDialog()));
        } else {

            mDataManager
                    .userManager()
                    .insertContact(mCurrentContact);

            mCompositeDisposable.add(mDataManager
                    .networkHelper()
                    .insertContact(mCurrentContact.getId(), mCurrentContact.getName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rows -> {
                        mView.showUserAddedToast();
                        mView.dismissDialog();
                    }));

        }

    }
}
