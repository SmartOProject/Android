package com.android.smarto.architecture.registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.utils.ImageUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.android.smarto.Constants.RESULT_LOAD_IMAGE;

public class RegisterActivity extends BaseActivity implements IRegisterActivity{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Uri mProfileImageUri;

    @BindView(R.id.button_register)                 ImageButton mRegisterButton;
    @BindView(R.id.register_mobile)                 EditText    mRegisterMobile;
    @BindView(R.id.register_password)               EditText    mRegisterPassword;
    @BindView(R.id.register_first_name)             EditText    mRegisterFirstName;
    @BindView(R.id.register_last_name)              EditText    mRegisterLastName;
    @BindView(R.id.register_confirm_password)       EditText    mRegisterConfirmPassword;
    @BindView(R.id.register_password_helper)        TextView    mRegisterPasswordHelper;
    @BindView(R.id.profile_image)                   ImageView   mProfileImage;

    @Inject
    RegisterPresenter<IRegisterActivity> mRegisterPresenter;

    private MaskedTextChangedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        initializeObservables();
        initMaskedListener();

    }

    private void initMaskedListener() {
        listener = new MaskedTextChangedListener(
                "+7 ([000]) [000] [00] [00]",
                true,
                mRegisterMobile,
                null,
                (maskFilled, extractedValue) -> {}
        );
        mRegisterMobile.addTextChangedListener(listener);
        mRegisterMobile.setOnFocusChangeListener(listener);
    }

    private void init(){

        Log.i(TAG, "onCreate()");
        ButterKnife.bind(this);
        mRegisterPresenter.onAttach(this);

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
                String email = mRegisterMobile.getText().toString();
                String password = mRegisterPassword.getText().toString();
                String confirmPassword = mRegisterConfirmPassword.getText().toString();
                String firstName = mRegisterFirstName.getText().toString();
                String secondName = mRegisterLastName.getText().toString();
                mRegisterPresenter.onRegisterClicked(email, password, confirmPassword,
                        firstName, secondName, mProfileImageUri);
                break;
            case R.id.profile_image:
                mRegisterPresenter.onProfileImageClick(this);
        }

    }

    @Override
    public void showConfirmPasswordHelper(boolean show){
        if (show)
            mRegisterPasswordHelper.setVisibility(View.VISIBLE);
        else
            mRegisterPasswordHelper.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openHomeActivity(){
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showFieldEmptyError() {
        Toast.makeText(this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMobileInputError() {
        Toast.makeText(this, getString(R.string.error_incorrect_mobile_number), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showIncorrectConfirmPasswordError() {
        Toast.makeText(this, getString(R.string.error_incorrect_passwords), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserExistError() {
        Toast.makeText(this, getString(R.string.error_exist_user), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            mProfileImageUri = data.getData();

            try {
                mProfileImage.setImageBitmap(ImageUtils.convertToBitmap(mProfileImageUri, this));
                Log.i(TAG, ImageUtils.convertToBitmap(mProfileImageUri, this).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //            mProfileImage.setImageURI(mProfileImageUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterPresenter.onDetach();
    }
}
