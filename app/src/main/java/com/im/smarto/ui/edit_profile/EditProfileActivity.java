package com.im.smarto.ui.edit_profile;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends BaseActivity implements IEditProfileActivity {

    @BindView(R.id.edit_profile_header) TextView mTopLabel;
    @BindView(R.id.changed_field) EditText mChangedField;
    @BindView(R.id.field_helper) TextView mFieldHelper;

    private String mType;

    private MaskedTextChangedListener listener;

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

        listener = new MaskedTextChangedListener(
                "+7 ([000]) [000] [00] [00]",
                true,
                mChangedField,
                null,
                (maskFilled, extractedValue) -> {}
        );
        mChangedField.addTextChangedListener(listener);
        mChangedField.setOnFocusChangeListener(listener);
        mChangedField.setHint(listener.placeholder());

        mTopLabel.setText("Edit phone");
        mChangedField.setHint("Phone");
        mChangedField.setInputType(InputType.TYPE_CLASS_PHONE);
        mFieldHelper.setText("Enter new phone. If you dont need than click back.");
    }
}
