package com.im.smarto.ui.edit_profile;

import android.util.Base64;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.base.BasePresenter;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 28.03.2018.
 */

public class EditProfilePresenter<V extends IEditProfileActivity> extends BasePresenter<V> {

    private static final String TAG = EditProfilePresenter.class.getSimpleName();

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

                mDataManager
                        .networkHelper()
                        .updateUser(s, Constants.FIRST_NAME_TYPE)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rows -> Log.i(TAG, "Rows affected: " + rows.getRowsAffected()),
                                error -> Log.i(TAG, error.getMessage()));

                mView.finish();
                break;
            case Constants.LAST_NAME:
                mDataManager.userManager().getCurrentUser().setLastName(s);

                mDataManager
                        .networkHelper()
                        .updateUser(s, Constants.LAST_NAME_TYPE)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rows -> Log.i(TAG, "Rows affected: " + rows.getRowsAffected()),
                                error -> Log.i(TAG, error.getMessage()));

                mView.finish();
                break;
            case Constants.PHONE:
                mDataManager.userManager().getCurrentUser().setPhone(s);

                String newBasic = "";
                try {
                    byte[] data = Base64
                            .decode(mDataManager
                                    .networkHelper()
                                    .getHeader()
                                    .split(" ")[1]
                                    .getBytes(), Base64.DEFAULT);
                    newBasic = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, newBasic);

                String creditionals = s + ":" + newBasic.split(":")[1];

                String basic = "Basic " + Base64.encodeToString(creditionals.getBytes(), Base64.NO_WRAP);

                mDataManager
                        .networkHelper()
                        .updateUser(s, Constants.PHONE_TYPE)
                        .subscribeOn(Schedulers.io())
                        .subscribe(rows -> {
                                    Log.i(TAG, "Rows affected: " + rows.getRowsAffected());
                                    mDataManager.networkHelper().setHeader(basic);
                                },
                                error -> Log.i(TAG, error.getMessage()));

                mView.finish();
                break;
        }
    }
}
