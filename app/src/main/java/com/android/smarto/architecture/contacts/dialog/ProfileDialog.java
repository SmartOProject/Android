package com.android.smarto.architecture.contacts.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anatoly Chernyshev on 24.02.2018.
 */

public class ProfileDialog extends Dialog {

    @BindView(R.id.dialog_profile_image) ImageView mProfileImage;
    @BindView(R.id.dialog_profile_username) TextView mProfileUsername;
    @BindView(R.id.dialog_profile_mobile) TextView mProfileMobile;
    @BindView(R.id.add_delete_btn) ImageView mAddDeleteBtn;

    private ProfileBtnAddRemoveClick mProfileBtnAddRemoveClick;

    private Context mContext;
    private String mImagePath;
    private String mUsername;
    private String mMobileNumber;
    private String mIconIndicator;

    public ProfileDialog(@NonNull Context context, String imagePath,
                         String username, String mobileNumber,
                         ProfileBtnAddRemoveClick profileBtnAddRemoveClick,
                         String iconIndicator) {
        super(context);
        this.mContext = context;
        this.mImagePath = imagePath;
        this.mUsername = username;
        this.mMobileNumber = mobileNumber;
        this.mProfileBtnAddRemoveClick = profileBtnAddRemoveClick;
        this.mIconIndicator = iconIndicator;
    }

    @OnClick(R.id.add_delete_btn)
    void onClick(){
        mProfileBtnAddRemoveClick.onProfileAddRemoveClick(mIconIndicator, mMobileNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_profile_dialog);

        ButterKnife.bind(this);

        Picasso.with(mContext).load(mImagePath)
                .resize(300, 300)
                .centerCrop().into(mProfileImage);

        mProfileUsername.setText(mUsername);
        mProfileMobile.setText(mMobileNumber);

        if (mIconIndicator.equals(Constants.DIALOG_DELETE_ICON)) {
            mAddDeleteBtn.setImageResource(R.drawable.ic_delete_black_24dp);
            mAddDeleteBtn.setBackground(mContext.getResources().getDrawable(R.drawable.circle_image_profile_dialog_white));
        }
            else {
            mAddDeleteBtn.setImageResource(R.drawable.ic_person_add_black_24dp);
            mAddDeleteBtn.setBackground(mContext.getResources().getDrawable(R.drawable.circle_image_profile_dialog));
        }

    }

    public void changeIcon(){
        if (mIconIndicator.equals(Constants.DIALOG_DELETE_ICON)) {
            mIconIndicator = Constants.DIALOG_ADD_ICON;
            mAddDeleteBtn.setBackground(mContext.getResources().getDrawable(R.drawable.circle_image_profile_dialog));
            mAddDeleteBtn.setImageResource(R.drawable.ic_person_add_black_24dp);
        } else {
            mIconIndicator = Constants.DIALOG_DELETE_ICON;
            mAddDeleteBtn.setBackground(mContext.getResources().getDrawable(R.drawable.circle_image_profile_dialog_white));
            mAddDeleteBtn.setImageResource(R.drawable.ic_delete_black_24dp);
        }
    }

}
