package com.android.smarto.architecture.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.smarto.R;
import com.android.smarto.prefs.SharedPreferencesRepository;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferencesRepository sharedPreferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        sharedPreferencesRepository = new SharedPreferencesRepository(getApplicationContext());

    }

    @OnClick(R.id.button_delete)
    void OnClick(){

        sharedPreferencesRepository.setLoggedIn(false);

    }
}
