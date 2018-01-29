package com.android.smarto.architecture.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.smarto.R;
import com.android.smarto.architecture.home.HomeActivity;
import com.android.smarto.prefs.SharedPreferencesRepository;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferencesRepository sharedPreferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferencesRepository = new SharedPreferencesRepository(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_register)
    void OnClick(){

        sharedPreferencesRepository.setLoggedIn(true);
        startActivity(new Intent(this, HomeActivity.class));
        finish();

    }

}
