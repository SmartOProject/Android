package com.im.smarto.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.navigation.NavigationActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class RegisterActivity extends BaseActivity implements IRegisterActivity{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.button_register)                 Button mRegisterButton;
    @BindView(R.id.register_mobile)                 EditText    mRegisterMobile;
    @BindView(R.id.register_password)               EditText    mRegisterPassword;
    @BindView(R.id.register_first_name)             EditText    mRegisterFirstName;
    @BindView(R.id.register_last_name)              EditText    mRegisterLastName;
    @BindView(R.id.register_confirm_password)       EditText    mRegisterConfirmPassword;
    @BindView(R.id.register_password_helper)        TextView    mRegisterPasswordHelper;

    @Inject
    RegisterPresenter<IRegisterActivity> mRegisterPresenter;

    private MaskedTextChangedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        initializeObservables();
        initMaskedListener();

    }

    private void initMaskedListener() {
        listener = new MaskedTextChangedListener(
                "+7 ([000]) [000] [00] [00]",
                true,
                mRegisterMobile,
                null,
                (maskFilled, extractedValue) -> {}
        );
        mRegisterMobile.addTextChangedListener(listener);
        mRegisterMobile.setOnFocusChangeListener(listener);
    }

    private void init(){

        Log.i(TAG, "onCreateView()");
        ButterKnife.bind(this);
        mRegisterPresenter.onAttach(this);

    }

    public void initializeObservables(){
        Observable passwordObservable = RxTextView.textChanges(mRegisterPassword).skip(1);
        Observable passwordConfirmObservable = RxTextView.textChanges(mRegisterConfirmPassword).skip(1);
        mRegisterPresenter.setObservables(passwordObservable, passwordConfirmObservable);
    }

    @OnClick(R.id.button_register)
    void OnClick(View view){
        switch (view.getId()){
            case R.id.button_register:
                String phoneNumber = mRegisterMobile.getText().toString();
                String password = mRegisterPassword.getText().toString();
                String confirmPassword = mRegisterConfirmPassword.getText().toString();
                String firstName = mRegisterFirstName.getText().toString();
                String lastName = mRegisterLastName.getText().toString();
                mRegisterPresenter.onRegisterClicked(phoneNumber, password, confirmPassword,
                        firstName, lastName);
                break;
        }
    }

    @Override
    public void showConfirmPasswordHelper(boolean show){
        if (show)
            mRegisterPasswordHelper.setVisibility(View.VISIBLE);
        else
            mRegisterPasswordHelper.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openNavigationActivity(){
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showFieldEmptyError() {
        Toast.makeText(this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMobileInputError() {
        Toast.makeText(this, getString(R.string.error_incorrect_mobile_number), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectConfirmPasswordError() {
        Toast.makeText(this, getString(R.string.error_incorrect_passwords), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserExistError() {
        Toast.makeText(this, getString(R.string.error_exist_user), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPhoneExistDialog() {
        Toast.makeText(this, "Phone already exist, choose another", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterPresenter.onDetach();
    }
}
