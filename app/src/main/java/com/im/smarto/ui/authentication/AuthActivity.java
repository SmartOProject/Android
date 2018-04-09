package com.im.smarto.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.navigation.NavigationActivity;
import com.im.smarto.ui.registration.RegisterActivity;
import com.lamudi.phonefield.PhoneEditText;
import com.lamudi.phonefield.PhoneInputLayout;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity implements IAuthActivity{

    private static final String TAG = AuthActivity.class.getSimpleName();

    @BindView(R.id.auth_progress_bar)       ProgressWheel mProgressBar;
    @BindView(R.id.auth_phone_number)       PhoneInputLayout mPhoneNumber;
    @BindView(R.id.button_login)            Button mLoginBtn;
    @BindView(R.id.auth_helper_text)        TextView mHelperText;
    @BindView(R.id.button_google)           Button mGoogleLoginBtn;
    @BindView(R.id.button_facebook)         Button mFacebookLoginBtn;
    @BindView(R.id.sign_up_button)          Button mSignUpBtn;
    @BindView(R.id.auth_password)           EditText mPassword;
    @BindView(R.id.auth_remember_check_box) CheckBox mIsRememberUser;

    @Inject
    AuthPresenter<IAuthActivity> mAuthPresenter;

    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();

    }

    public void init(){
        Log.i(TAG, "onCreateView()");
        ButterKnife.bind(this);
        mAuthPresenter.onAttach(this);
        mPhoneNumber.setHint(R.string.phone_number);
        mPhoneNumber.setDefaultCountry("US");
    }

    @OnClick({R.id.button_login, R.id.button_google, R.id.button_facebook, R.id.sign_up_button})
    void onClickButton(View view){
        switch (view.getId()){
            case R.id.button_login:

                boolean valid = true;

                // checks if the field is valid
                if (mPhoneNumber.isValid()) {
                    mPhoneNumber.setError(null);
                } else {
                    valid = false;
                }

                if (valid) {
                    Toast.makeText(AuthActivity.this, R.string.correct_phone, Toast.LENGTH_LONG).show();

                    String phoneNumber = mPhoneNumber.getPhoneNumber();
                    String password = mPassword.getText().toString();
                    boolean isNeedToRemember = mIsRememberUser.isChecked();
                    mAuthPresenter.onLoginClicked(phoneNumber, password, isNeedToRemember);
                } else {
                    Toast.makeText(AuthActivity.this, R.string.incorrect_phone, Toast.LENGTH_LONG).show();
                }
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
        hideLoginFields();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoginFields() {
        mLoginBtn.setVisibility(View.INVISIBLE);
        mIsRememberUser.setVisibility(View.INVISIBLE);
        mFacebookLoginBtn.setVisibility(View.INVISIBLE);
        mGoogleLoginBtn.setVisibility(View.INVISIBLE);
        mSignUpBtn.setVisibility(View.INVISIBLE);
        mHelperText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        showLoginFields();
    }

    private void showLoginFields() {
        mLoginBtn.setVisibility(View.VISIBLE);
        mIsRememberUser.setVisibility(View.VISIBLE);
        mFacebookLoginBtn.setVisibility(View.VISIBLE);
        mGoogleLoginBtn.setVisibility(View.VISIBLE);
        mSignUpBtn.setVisibility(View.VISIBLE);
        mHelperText.setVisibility(View.VISIBLE);
    }
}
