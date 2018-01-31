package com.android.smarto.architecture.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.smarto.R;
import com.android.smarto.app.App;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.prefs.ISharedPreferencesRepository;
import com.android.smarto.retrofit.Test;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    @Inject
    ISharedPreferencesRepository mSharedPreferencesRepository;

    @Inject
    Retrofit mRetrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }

    private void init() {

        ButterKnife.bind(this);
        App.get().getApplicationComponent().inject(this);

    }

    @OnClick({R.id.button_delete, R.id.button_request})
    void OnClick(View view){

        switch (view.getId()){

            case R.id.button_delete:
                mSharedPreferencesRepository.setLoggedIn(false);
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                break;

            case R.id.button_request:
                mRetrofitClient.create(Test.class).testRequest().enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        //nothing
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        //nothing
                    }
                });
                break;

        }

    }
}
