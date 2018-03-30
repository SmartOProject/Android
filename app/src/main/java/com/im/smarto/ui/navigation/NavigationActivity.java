package com.im.smarto.ui.navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.authentication.AuthActivity;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.fragments.ChatFragment;
import com.im.smarto.ui.contacts.ContactFragment;
import com.im.smarto.ui.home.HomeFragment;
import com.im.smarto.ui.map.MapFragment;
import com.im.smarto.ui.profile.ProfileActivity;
import com.im.smarto.ui.task.TaskFragment;
import com.im.smarto.db.entities.User;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends BaseActivity implements INavigationActivity,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NavigationActivity.class.getSimpleName();

    private static final int LOCATION_REQUEST = 1000;

    private ActionBarDrawerToggle mToggle;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Inject
    NavigationPresenter<INavigationActivity> mNavigationPresenter;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        init();

        mBottomNavigationView.setOnNavigationItemSelectedListener( item -> {

            switch (item.getItemId()){
                case R.id.home_item:
                    mNavigationPresenter.onHomeClicked();
                    return true;
                case R.id.contacts_item:
                    mNavigationPresenter.onContactClicked();
                    return true;
                case R.id.chat_item:
                    mNavigationPresenter.onChatClicked();
                    return true;
                case R.id.tasks_item:
                    mNavigationPresenter.onTaskClicked();
                    return true;
                case R.id.map_item:
                    mNavigationPresenter.onMapClicked();
                    return true;
            }
            return false;
        });

    }

    private FragmentTransaction createFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        return fragmentTransaction;
    }

    @Override
    public void initNavigationBar() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);

        requestExternalReadPermissions();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_normal)));
    }

    private void requestExternalReadPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationPresenter.onResume();
    }

    private void setHeader() {
        View headerLayout = mNavigationView.getHeaderView(0);
        CircleImageView navImage = headerLayout.findViewById(R.id.nav_image_view);
        TextView navUserName = headerLayout.findViewById(R.id.nav_user_name);
        TextView navUserEmail = headerLayout.findViewById(R.id.nav_user_email);

        navUserName.setText(mCurrentUser.getName());
        navUserEmail.setText(mCurrentUser.getPhone());
        if (mCurrentUser.getImgUrl() != null){
            Picasso.with(getApplicationContext()).load(mCurrentUser.getImgUrl())
                    .resize(200,200)
                    .centerCrop().into(navImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setHeader();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){

        Log.i(TAG, "onCreateView()");
        ButterKnife.bind(this);
        mNavigationPresenter.onAttach(this);

        mNavigationPresenter.onCreate();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                mNavigationPresenter.onLogoutClicked();
                break;
            case R.id.profile_menu_item:
                mNavigationPresenter.onProfileClicked();
                break;

        }
        return true;
    }

    @Override
    public void showHomeFragment() {
        getSupportActionBar().setTitle(getString(R.string.home_title));
        createFragmentTransaction().replace(R.id.content, new HomeFragment(), HomeFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showChatFragment() {
        getSupportActionBar().setTitle(getString(R.string.chat_title));
        createFragmentTransaction().replace(R.id.content, new ChatFragment(), ChatFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showTaskFragment() {
        getSupportActionBar().setTitle(getString(R.string.tasks_title));
        createFragmentTransaction().replace(R.id.content, new TaskFragment(), TaskFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showMapFragment() {
        getSupportActionBar().setTitle(getString(R.string.map_title));
        createFragmentTransaction().replace(R.id.content, new MapFragment(), MapFragment.class.getSimpleName())
                    .commit();
    }

    @Override
    public void showContactFragment() {
        getSupportActionBar().setTitle(getString(R.string.contacts_title));
        createFragmentTransaction().replace(R.id.content, new ContactFragment(), ContactFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void signOut() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void openProfileActivity() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void setupNavHeader(User currentUser) {
        mCurrentUser = currentUser;
        setHeader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNavigationPresenter.onDetach();
    }
}
