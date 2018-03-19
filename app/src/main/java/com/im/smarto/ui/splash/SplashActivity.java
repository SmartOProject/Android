package com.im.smarto.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.ui.authentication.AuthActivity;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.navigation.NavigationActivity;

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

    /**
     * Putting userToken to Navigation Activity.
     */

    @Override
    public void openNavigationActivity(String token){
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(Constants.USER_TOKEN, token);
        startActivity(intent);
        finish();
    }

    @Override
    public void openAuthActivity(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mSplashPresenter.onDetach();
    }

}
