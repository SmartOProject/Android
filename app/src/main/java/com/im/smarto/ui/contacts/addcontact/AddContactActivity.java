package com.im.smarto.ui.contacts.addcontact;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.im.smarto.R;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.contacts.ContactsAdapter;
import com.im.smarto.ui.contacts.dialog.ProfileBtnAddRemoveClick;
import com.im.smarto.ui.contacts.dialog.ProfileDialog;
import com.im.smarto.db.entities.User;
import com.im.smarto.utils.KeyboardUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public class AddContactActivity extends BaseActivity implements IAddContactActivity,
        SearchView.OnQueryTextListener, ProfileBtnAddRemoveClick {

    public static final String TAG = AddContactActivity.class.getSimpleName();

    @BindView(R.id.add_contact_recycler_view) RecyclerView mAddContactRecyclerView;
    @BindView(R.id.add_contact_progress) ProgressWheel mProgressBar;
    @BindView(R.id.add_contact_helper) TextView mContactHelper;

    @Inject
    AddContactPresenter<IAddContactActivity> mAddContactPresenter;

    private ProfileDialog mProfileDialog;

    private RecyclerView.LayoutManager mLayoutManager;
    private ContactsAdapter mContactsAdapter;
    private Snackbar mSnackBar;

    private List<User> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mAddContactPresenter.onAttach(this);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mData = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddContactPresenter.onViewResumed();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAddContactPresenter.onQueryTextSubmit(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAddContactPresenter.onQueryTextChange(newText);
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddContactPresenter.onDetach();
    }

    @Override
    public void setupRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContactsAdapter = new ContactsAdapter(mData, this, this);
        mAddContactRecyclerView.setLayoutManager(mLayoutManager);
        mAddContactRecyclerView.setAdapter(mContactsAdapter);
    }

    @Override
    public void onClick(User user) {
        mAddContactPresenter.onItemClicked(user);
    }

    @Override
    public void showProfileDialog(String imagePath,
                                  String username,
                                  String mobileNumber,
                                  String icon) {
        mProfileDialog = new ProfileDialog(this, imagePath, username, mobileNumber,
                this, icon);
        mProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProfileDialog.setCancelable(true);
        mProfileDialog.show();
    }

    @Override
    public void changeDialogIcon() {
        mProfileDialog.changeIcon();
    }

    @Override
    public void hideContactHelper() {
        mContactHelper.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        mAddContactPresenter.onProfileAddRemoveClick(icon, mobileNumber);
    }

    @Override
    public void updateData(List<User> data) {
        mContactsAdapter.updateList(data);
    }

    @Override
    public void showContactAddedSnackBar() {
        mSnackBar = Snackbar.make(findViewById(R.id.add_contact_container),
                "User added!", Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

    @Override
    public void hideKeyboard() {
        KeyboardUtils.hideSoftInput(this);
    }
}
