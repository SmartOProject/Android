package com.im.smarto.ui.contacts.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.contacts.ContactsPresenter;
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
    @BindView(R.id.trust_checkbox) CheckBox mTrustCheckBox;

    private ProfileBtnAddRemoveClick mProfileBtnAddRemoveClick;

    private ContactsPresenter mContactsPresenter;

    private Context mContext;
    private String mImagePath;
    private String mUsername;
    private String mMobileNumber;
    private String mIconIndicator;
    private int trustId;

    public ProfileDialog(@NonNull Context context, String imagePath,
                         String username, String mobileNumber,
                         ProfileBtnAddRemoveClick profileBtnAddRemoveClick,
                         String iconIndicator, ContactsPresenter presenter,
                         int trustId) {
        super(context);
        this.mContext = context;
        this.mImagePath = imagePath;
        this.mUsername = username;
        this.mMobileNumber = mobileNumber;
        this.mProfileBtnAddRemoveClick = profileBtnAddRemoveClick;
        this.mIconIndicator = iconIndicator;
        this.mContactsPresenter = presenter;
        this.trustId = trustId;
    }

    @OnClick({R.id.add_delete_btn, R.id.trust_checkbox})
    void onClick(View v){
        switch (v.getId()){
            case R.id.trust_checkbox:
                mContactsPresenter.onCheckBoxClicked(mTrustCheckBox.isChecked());
                break;
            case R.id.add_delete_btn:
                mProfileBtnAddRemoveClick.onProfileAddRemoveClick(mIconIndicator, mMobileNumber);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_profile_dialog);

        ButterKnife.bind(this);

        if (trustId == 1) mTrustCheckBox.setChecked(true);
        else mTrustCheckBox.setChecked(false);

        Picasso.with(mContext).load(mImagePath)
                .resize(300, 300)
                .placeholder(R.drawable.profile_image)
                .centerCrop().into(mProfileImage);

        if (mContactsPresenter == null) mTrustCheckBox.setVisibility(View.INVISIBLE);

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
