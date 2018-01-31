package com.android.smarto.architecture.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.smarto.app.App;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.home.HomeActivity;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity implements ISplashContract.ISplashActivity {

    @Inject
    ISplashContract.ISplashPresenter<ISplashContract.ISplashActivity> mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        mSplashPresenter.isLoggedIn();

    }

    private void init() {

        App.get().getPresenterComponent().inject(this);
        mSplashPresenter.onAttach(this);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSplashPresenter.onDetach();
    }

    @Override
    public void showProgress() {
        //nothing
    }

    @Override
    public void hideProgress() {
        //nothing
    }
}
