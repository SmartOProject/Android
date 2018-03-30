package com.im.smarto.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.edit_profile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements IProfileActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @BindView(R.id.profile_image) ImageView mProfileImage;
    @BindView(R.id.first_name) TextView mProfileFirstName;
    @BindView(R.id.last_name) TextView mProfileLastName;
    @BindView(R.id.phone) TextView mProfilePhone;

    @Inject
    ProfilePresenter<IProfileActivity> mProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProfilePresenter.onAttach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProfilePresenter.onCreate();
    }

    @OnClick({R.id.first_name, R.id.last_name, R.id.phone, R.id.email, R.id.fab_load_photo})
    void onClick(View v){
        Intent intent = new Intent(this, EditProfileActivity.class);
        switch (v.getId()){
            case R.id.first_name:
                intent.putExtra(Constants.EDIT_PROFILE_TYPE, Constants.FIRST_NAME);
                startActivity(intent);
                break;
            case R.id.last_name:
                intent.putExtra(Constants.EDIT_PROFILE_TYPE, Constants.LAST_NAME);
                startActivity(intent);
                break;
            case R.id.phone:
                intent.putExtra(Constants.EDIT_PROFILE_TYPE, Constants.PHONE);
                startActivity(intent);
                break;
            case R.id.email:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab_load_photo:
                Toast.makeText(this, "There is no web server yet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setProfileImage(String imgUrl) {
        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.profile_image)
                .resize(300, 300)
                .centerCrop()
                .into(mProfileImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) this.finish();
        return true;
    }

    @Override
    public void setFirstName(String firstName) {
        mProfileFirstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        mProfileLastName.setText(lastName);
    }

    @Override
    public void setMobile(String phone) {
        mProfilePhone.setText(phone);
    }
}
