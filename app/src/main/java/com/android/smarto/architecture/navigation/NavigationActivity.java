package com.android.smarto.architecture.navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.authentication.AuthActivity;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.fragments.ChatFragment;
import com.android.smarto.architecture.fragments.ContactFragment;
import com.android.smarto.architecture.fragments.HomeFragment;
import com.android.smarto.architecture.fragments.MapFragment;
import com.android.smarto.architecture.task.TaskFragment;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.data.IDataManager;
import com.android.smarto.db.model.User;
import com.android.smarto.utils.ImageUtils;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.smarto.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class NavigationActivity extends BaseActivity implements INavigationActivity,
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = NavigationActivity.class.getSimpleName();

    private ActionBarDrawerToggle mToggle;
    private User mCurrentUser;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Inject
    NavigationPresenter<INavigationActivity> mNavigationPresenter;

    @Inject
    IDataManager mDataManager;
    @Inject
    TaskData mTaskData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        init();
        initNavigationBar();

        mNavigationPresenter.onCreate();

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

        requestExternalReadPermissions();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

    }

    private void requestExternalReadPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            takeHeader();
        }

    }

    private void takeHeader() {
        View headerLayout = mNavigationView.getHeaderView(0);
        ImageView navImage = headerLayout.findViewById(R.id.nav_image_view);
        TextView navUserName = headerLayout.findViewById(R.id.nav_user_name);
        TextView navUserEmail = headerLayout.findViewById(R.id.nav_user_email);

        navUserName.setText(mCurrentUser.getFirstName() + " " + mCurrentUser.getSecondName());
        navUserEmail.setText(mCurrentUser.getEmail());
        if (mCurrentUser.getProfileImagePath() != null)
            try {
                navImage.setImageBitmap(ImageUtils.convertToBitmap(Uri.parse(mCurrentUser.getProfileImagePath()), this));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeHeader();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
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

        Log.i(TAG, "onCreate()");
        ButterKnife.bind(this);
        mNavigationPresenter.onAttach(this);

        mCurrentUser = mDataManager.getCurrentUser();

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

    public void onGroupItemRemoved(int groupPosition) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_group_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, v -> onItemUndoActionClicked());
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    public void onChildItemRemoved(int groupPosition, int childPosition) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_child_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, v -> onItemUndoActionClicked());
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    private void onItemUndoActionClicked() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TaskFragment.class.getSimpleName());
        final long result = mTaskData.undoLastRemoval();

        if (result == RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION) {
            return;
        }

        final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(result);
        final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(result);

        if (childPosition == RecyclerView.NO_POSITION) {
            // group item
            ((TaskFragment) fragment).notifyGroupItemRestored(groupPosition);
        } else {
            // child item
            ((TaskFragment) fragment).notifyChildItemRestored(groupPosition, childPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNavigationPresenter.onDetach();
    }
}
