package com.android.smarto.architecture.registration;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.app.App;
import com.android.smarto.architecture.home.HomeActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.android.smarto.Constants.RESULT_LOAD_IMAGE;

public class RegisterActivity extends AppCompatActivity implements IRegisterContract.IRegisterActivity{

    @BindView(R.id.button_register)
    Button mRegisterButton;
    @BindView(R.id.register_username)
    EditText mRegisterUsername;
    @BindView(R.id.register_password)
    EditText mRegisterPassword;
    @BindView(R.id.register_confirm_password)
    EditText mRegisterConfirmPassword;
    @BindView(R.id.register_password_helper)
    TextView mRegisterPasswordHelper;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;

    @Inject
    IRegisterContract.IRegisterPresenter<IRegisterContract.IRegisterActivity> mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }

    private void init() {

        ButterKnife.bind(this);
        App.get().getPresenterComponent().inject(this);
        mRegisterPresenter.onAttach(this);

        initializeObservables();

    }

    public void initializeObservables(){
        Observable passwordObservable = RxTextView.textChanges(mRegisterPassword).skip(1);
        Observable passwordConfirmObservable = RxTextView.textChanges(mRegisterConfirmPassword).skip(1);
        mRegisterPresenter.setObservables(passwordObservable, passwordConfirmObservable);
    }

    @OnClick({R.id.button_register, R.id.profile_image})
    void OnClick(View view){

        switch (view.getId()){

            case R.id.button_register:
                String login = mRegisterUsername.getText().toString();
                String password = mRegisterPassword.getText().toString();
                String confirmPassword = mRegisterConfirmPassword.getText().toString();
                mRegisterPresenter.onRegisterClick(login, password, confirmPassword);
                break;
            case R.id.profile_image:
                mRegisterPresenter.onProfileImageClick(this);
        }


    }

    @Override
    public void showProgress() {
        //nothing
    }

    @Override
    public void hideProgress() {
        //nothing
    }

    @Override
    public void showConfirmPasswordHelper(boolean show) {
        if (show)
            mRegisterPasswordHelper.setVisibility(View.VISIBLE);
        else
            mRegisterPasswordHelper.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showError() {
        Toast.makeText(this, getString(R.string.incorrect_data_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            mProfileImage.setImageURI(imageUri);

        }
    }
}
