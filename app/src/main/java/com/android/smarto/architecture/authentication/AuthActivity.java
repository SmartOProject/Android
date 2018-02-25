package com.android.smarto.architecture.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity implements IAuthActivity{

    private static final String TAG = AuthActivity.class.getSimpleName();

    @BindView(R.id.edit_text_login)     EditText mLoginEditText;
    @BindView(R.id.edit_text_password)  EditText mPasswordEditText;

    @Inject
    AuthPresenter<IAuthActivity> mAuthPresenter;

    private MaskedTextChangedListener listener;

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
                mLoginEditText,
                null,
                (maskFilled, extractedValue) -> {}
        );
        mLoginEditText.addTextChangedListener(listener);
        mLoginEditText.setOnFocusChangeListener(listener);
        mLoginEditText.setHint(listener.placeholder());
    }

    public void init(){

        Log.i(TAG, "onCreate()");
        ButterKnife.bind(this);
        mAuthPresenter.onAttach(this);

    }

    @OnClick({R.id.button_login, R.id.button_sign_up, R.id.button_google,
                    R.id.button_facebook})
    void onClickButton(View view){

        switch (view.getId()){
            case R.id.button_login:
                String login = mLoginEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                mAuthPresenter.onLoginClicked(login, password);
                break;
            case R.id.button_sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
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
    public void openHomeActivity(){
        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }

    @Override
    public void showEmptyMobileNumberError(){
        Toast.makeText(this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectLoginDataError(){
        Toast.makeText(this, getString(R.string.incorrect_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthPresenter.onDetach();
    }
}
