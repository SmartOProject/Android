package com.android.smarto.architecture.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements ISplashActivity{

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    SplashPresenter<ISplashActivity> mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            Log.i(TAG, "auth.getCurrentUser().getIdToken() - " + auth.getCurrentUser().getUid());
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("currentUserID", auth.getCurrentUser().getUid());
            startActivity(intent);
            finish();
        } else {
            Log.i(TAG, "auth.getCurrentUser().getPhoneNumber().isEmpty()");
            openAuthActivity();
            finish();
        }

        init();
        //mSplashPresenter.onCreate();

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
