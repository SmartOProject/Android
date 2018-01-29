package com.android.smarto.architecture.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.home.HomeActivity;

public class SplashActivity extends AppCompatActivity implements ISplashContract.ISplashActivity {

    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashPresenter = new SplashPresenter(this, getApplicationContext());
        splashPresenter.isLoggedIn();

    }

    @Override
    public void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void openAuthActivity() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
