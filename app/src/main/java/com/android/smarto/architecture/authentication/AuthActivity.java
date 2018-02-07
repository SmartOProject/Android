package com.android.smarto.architecture.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.app.App;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.registration.RegisterActivity;
import com.android.smarto.data.UserData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements IAuthActivity{

    @BindView(R.id.edit_text_login)     EditText mLoginEditText;
    @BindView(R.id.edit_text_password)  EditText mPasswordEditText;

    @Inject
    AuthPresenter<IAuthActivity> mAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();

    }

    public void init(){

        App.get().getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        mAuthPresenter.onAttach(this);

    }

    @OnClick({R.id.button_login, R.id.button_sign_up, R.id.button_google,
                    R.id.button_facebook})
    void onClickButton(View view){

        switch (view.getId()){

            case R.id.button_login:
                mAuthPresenter.confirmUserData();
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
    public void showEmptyLoginDataError(){
        Toast.makeText(this, getString(R.string.empty_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectLoginDataError(){
        Toast.makeText(this, getString(R.string.incorrect_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public UserData getUserData(){

        String login = mLoginEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        return new UserData(login, password);

    }

}
