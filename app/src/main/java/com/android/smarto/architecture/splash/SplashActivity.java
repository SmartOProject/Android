package com.android.smarto.architecture.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements ISplashActivity{

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    SplashPresenter<ISplashActivity> mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        init();
        mSplashPresenter.onCreate();

    }

    private void init(){

        Log.i(TAG, "onCreate()");
        mSplashPresenter.onAttach(this);

    }

    @Override
    public void openNavigationActivity(){
        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }

    @Override
    public void openAuthActivity(){
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mSplashPresenter.onDetach();
    }

}
