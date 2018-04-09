package com.im.smarto.ui.edit_profile;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.registration.RegisterActivity;
import com.lamudi.phonefield.PhoneInputLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.im.smarto.Constants.PHONE;
import static com.im.smarto.Constants.PHONE_TYPE;

public class EditProfileActivity extends BaseActivity implements IEditProfileActivity {

    @BindView(R.id.edit_profile_header) TextView mTopLabel;
    @BindView(R.id.changed_field) EditText mChangedField;
    @BindView(R.id.edit_profile_phone) PhoneInputLayout mPhoneField;
    @BindView(R.id.field_helper) TextView mFieldHelper;

    private String mType;

    @Inject
    EditProfilePresenter<IEditProfileActivity> mEditProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        mEditProfilePresenter.onAttach(this);

        if (getIntent() != null) mType = getIntent().getStringExtra(Constants.EDIT_PROFILE_TYPE);

        mEditProfilePresenter.onCreate(mType);

    }

    @OnClick(R.id.edit_profile_save_button)
    void onClick(){

        if (mType.equals(PHONE)){

            boolean valid = true;

            // checks if the field is valid
            if (mPhoneField.isValid()) {
                mPhoneField.setError(null);
            } else {
                valid = false;
            }

            if (valid) {
                String phoneNumber = mPhoneField.getPhoneNumber();
                mEditProfilePresenter.onSaveClicked(phoneNumber);
            } else {
                Toast.makeText(EditProfileActivity.this, R.string.incorrect_phone,
                        Toast.LENGTH_LONG).show();
            }

        } else
            mEditProfilePresenter.onSaveClicked(mChangedField.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEditProfilePresenter.onDetach();
    }

    @Override
    public void setupLayoutForEditFirstName() {
        mTopLabel.setText("Edit first name");
        mChangedField.setHint("First name");
        mFieldHelper.setText("Enter new first name. If you dont need than click back.");
    }

    @Override
    public void setupLayoutForEditLastName() {
        mTopLabel.setText("Edit last name");
        mChangedField.setHint("Last name");
        mFieldHelper.setText("Enter new last name. If you dont need than click back.");
    }

    @Override
    public void setupLayoutForEditPhone() {

        mChangedField.setVisibility(View.INVISIBLE);
        mPhoneField.setVisibility(View.VISIBLE);

        mTopLabel.setText("Edit phone");
        mPhoneField.setHint(R.string.phone_number);
        mPhoneField.setDefaultCountry("US");
        mFieldHelper.setText("Enter new phone. If you dont need than click back.");
    }
}
