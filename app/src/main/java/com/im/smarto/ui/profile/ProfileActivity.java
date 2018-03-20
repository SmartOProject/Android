package com.im.smarto.ui.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class ProfileActivity extends BaseActivity implements IProfileActivity {

    @BindView(R.id.change_email) Button mChangeEmailButton;
    @BindView(R.id.profile_email) TextView mEmailText;
    @BindView(R.id.change_phone) Button mChangePhoneButton;
    @BindView(R.id.profile_phone) TextView mPhoneText;
    @BindView(R.id.change_username) Button mChangeUsernameButton;
    @BindView(R.id.profile_username) TextView mUsernameText;

    @Inject
    ProfilePresenter<IProfileActivity> mProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfilePresenter.onAttach(this);
        mProfilePresenter.onCreate();
    }

    @Override
    public void showProfileImage() {

    }

    @Override
    public void showUsername(String username) {

    }

    @Override
    public void showMobile(String phone) {

    }
}
