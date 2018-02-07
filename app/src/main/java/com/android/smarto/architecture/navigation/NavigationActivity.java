package com.android.smarto.architecture.navigation;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.smarto.R;
import com.android.smarto.app.App;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.fragments.ChatFragment;
import com.android.smarto.architecture.fragments.ContactFragment;
import com.android.smarto.architecture.fragments.HomeFragment;
import com.android.smarto.architecture.fragments.MapFragment;
import com.android.smarto.prefs.ISharedPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements INavigationActivity,
        NavigationView.OnNavigationItemSelectedListener{

    private ActionBarDrawerToggle mToggle;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Inject
    NavigationPresenter<INavigationActivity> mNavigationPresenter;

    @Inject
    ISharedPreferencesRepository mSharedPreferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        init();
        initNavigationBar();

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

    private void initNavigationBar() {

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){

        App.get().getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        mNavigationPresenter.onAttach(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                mNavigationPresenter.onLogoutClicked();
                break;
        }
        return true;
    }

    @Override
    public void setHomeFragment() {
        createFragmentTransaction().replace(R.id.content, new HomeFragment(), HomeFragment.class.getSimpleName())
                .commit();
        getSupportActionBar().setTitle(getString(R.string.home_title));
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
        getSupportActionBar().setTitle(getString(R.string.map_title));
        createFragmentTransaction().replace(R.id.content, new MapFragment(), MapFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showMapFragment() {
        getSupportActionBar().setTitle(getString(R.string.tasks_title));
        createFragmentTransaction().replace(R.id.content, new TaskFragment(), TaskFragment.class.getSimpleName())
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
        mSharedPreferencesRepository.setLoggedIn(false);
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
