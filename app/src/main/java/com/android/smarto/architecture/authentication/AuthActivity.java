package com.android.smarto.architecture.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.home.HomeActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.android.smarto.data.UserData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements IAuthContract.IAuthActivity {

    @BindView(R.id.edit_text_login)     EditText mLoginEditText;
    @BindView(R.id.edit_text_password)  EditText mPasswordEditText;

    private AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();

    }

    public void init() {

        ButterKnife.bind(this);

        authPresenter = new AuthPresenter(this, getApplicationContext());

    }

    @OnClick({R.id.button_login, R.id.button_sign_up, R.id.button_google,
                    R.id.button_facebook, R.id.button_remind})
    void onClickButton(View view) {

        switch (view.getId()){

            case R.id.button_login:
                authPresenter.confirmUserData();
                break;
            case R.id.button_sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
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
            case R.id.button_remind:
                /**
                 * Show remind password dialog or activity.
                 */

        }

    }

    @Override
    public void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showEmptyLoginDataError() {
        Toast.makeText(this, getString(R.string.empty_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectLoginDataError() {
        Toast.makeText(this, getString(R.string.incorrect_login_error), Toast.LENGTH_SHORT).show();
    }


    @Override
    public UserData getUserData() {

        String login = mLoginEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        return new UserData(login, password);

    }

}
