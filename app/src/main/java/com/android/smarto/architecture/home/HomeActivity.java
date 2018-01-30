package com.android.smarto.architecture.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.smarto.R;
import com.android.smarto.architecture.authentication.AuthActivity;
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

    @OnClick({R.id.button_delete, R.id.button_request})
    void OnClick(View view){

        switch (view.getId()){

            case R.id.button_delete:
                sharedPreferencesRepository.setLoggedIn(false);
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                break;
            case R.id.button_request:

                break;

        }

    }
}
