package com.android.smarto.architecture.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity implements IAuthActivity{

    private static final String TAG = AuthActivity.class.getSimpleName();

    @BindView(R.id.auth_phone_number)       EditText mPhoneNumber;
    @BindView(R.id.auth_progress_bar)       ProgressWheel mProgressBar;
    @BindView(R.id.auth_helper_text)        TextView mHelperText;
    @BindView(R.id.button_google)
    ImageButton mGoogleLoginBtn;
    @BindView(R.id.button_facebook)         ImageButton mFacebookLoginBtn;
    @BindView(R.id.button_login)            ImageButton mLoginBtn;

    @Inject
    AuthPresenter<IAuthActivity> mAuthPresenter;

    private MaskedTextChangedListener listener;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i(TAG, "onVerification completed: " + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideProgressBar();
                Toast.makeText(AuthActivity.this, "Verification failed!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(s, forceResendingToken);
                Log.i(TAG, "onCodeSent()");
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.i(TAG, "onCodeAutoRetrivalTimeout");
            }
        };

        init();
        initMaskedListener();

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Success!!!");
                        FirebaseUser user = task.getResult().getUser();
                        Intent intent = new Intent(this, NavigationActivity.class);
                        intent.putExtra("currentUserID", user.getUid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Log.i(TAG, "FAILED!!!");
                    }
                });
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

        Log.i(TAG, "onCreate()");
        ButterKnife.bind(this);
        mAuthPresenter.onAttach(this);

    }

    @OnClick({R.id.button_login, R.id.button_google,
                    R.id.button_facebook})
    void onClickButton(View view){

        switch (view.getId()){
            case R.id.button_login:
                String phoneNumber = mPhoneNumber.getText().toString();
                showProgressBar();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallbacks);

                //mAuthPresenter.onLoginClicked(phoneNumber, password);
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

    @Override
    public void showProgressBar() {
        mPhoneNumber.setVisibility(View.INVISIBLE);
        mFacebookLoginBtn.setVisibility(View.INVISIBLE);
        mGoogleLoginBtn.setVisibility(View.INVISIBLE);
        mLoginBtn.setVisibility(View.INVISIBLE);
        mHelperText.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mPhoneNumber.setVisibility(View.VISIBLE);
        mFacebookLoginBtn.setVisibility(View.VISIBLE);
        mGoogleLoginBtn.setVisibility(View.VISIBLE);
        mLoginBtn.setVisibility(View.VISIBLE);
        mHelperText.setVisibility(View.VISIBLE);
    }
}
