package com.im.smarto.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.navigation.NavigationActivity;
import com.im.smarto.ui.registration.RegisterActivity;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity implements IAuthActivity{

    private static final String TAG = AuthActivity.class.getSimpleName();

    @BindView(R.id.auth_progress_bar)       ProgressWheel mProgressBar;
    @BindView(R.id.auth_phone_number)       EditText mPhoneNumber;
    @BindView(R.id.button_login)            Button mLoginBtn;
    @BindView(R.id.auth_helper_text)        TextView mHelperText;
    @BindView(R.id.button_google)           Button mGoogleLoginBtn;
    @BindView(R.id.button_facebook)         Button mFacebookLoginBtn;
    @BindView(R.id.auth_password)           EditText mPassword;
    @BindView(R.id.auth_remember_check_box) CheckBox mIsRememberUser;

    @Inject
    AuthPresenter<IAuthActivity> mAuthPresenter;

    private MaskedTextChangedListener listener;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();
        initMaskedListener();

    }

    private void initMaskedListener() {
        listener = new MaskedTextChangedListener(
                "+7 ([000]) [000] [00] [00]",
                true,
                mPhoneNumber,
                null,
                (maskFilled, extractedValue) -> {}
        );
        mPhoneNumber.addTextChangedListener(listener);
        mPhoneNumber.setOnFocusChangeListener(listener);
        mPhoneNumber.setHint(listener.placeholder());
    }

    public void init(){
        Log.i(TAG, "onCreateView()");
        ButterKnife.bind(this);
        mAuthPresenter.onAttach(this);
    }

    @OnClick({R.id.button_login, R.id.button_google, R.id.button_facebook, R.id.sign_up_button})
    void onClickButton(View view){
        switch (view.getId()){
            case R.id.button_login:
                String phone = mPhoneNumber.getText().toString();
                String password = mPassword.getText().toString();
                boolean isNeedToRemember = mIsRememberUser.isChecked();
                mAuthPresenter.onLoginClicked(phone, password, isNeedToRemember);
                break;
            case R.id.sign_up_button:
                mAuthPresenter.onSignUpButtonClicked();
                break;
            case R.id.button_google:
                /**
                 * Show Google log in dialog or activity.
                 */
                break;
            case R.id.button_facebook:
                /**
                 * Show Facebook log in dialog or activity.
                 */
                break;
        }
    }

    @Override
    public void openNavigationActivity(String token){
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(Constants.USER_TOKEN, token);
        startActivity(intent);
        finish();
    }

    @Override
    public void openRegisterActivity() {
        startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
    }

    @Override
    public void showIncorrectPhoneNumberError() {
        mSnackbar = Snackbar.make(findViewById(R.id.auth_container),
                "Incorrect Phone Number",
                Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    @Override
    public void showEmptyFieldsError() {
        mSnackbar = Snackbar.make(findViewById(R.id.auth_container),
                "Some of fields is empty",
                Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    @Override
    public void showAuthenticationFailedError() {
        mSnackbar = Snackbar.make(findViewById(R.id.auth_container),
                "Authentication failed",
                Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthPresenter.onDetach();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
