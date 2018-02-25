package com.android.smarto.architecture.contacts.addcontact;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.contacts.ContactsAdapter;
import com.android.smarto.architecture.contacts.dialog.ProfileBtnAddRemoveClick;
import com.android.smarto.architecture.contacts.dialog.ProfileDialog;
import com.android.smarto.db.model.User;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public class AddContactActivity extends BaseActivity implements IAddContactActivity,
        SearchView.OnQueryTextListener, ProfileBtnAddRemoveClick{

    public static final String TAG = AddContactActivity.class.getSimpleName();

    @BindView(R.id.add_contact_recycler_view) RecyclerView mAddContactRecyclerView;
    @BindView(R.id.add_contact_progress) ProgressBar mProgressBar;

    @Inject
    AddContactPresenter<IAddContactActivity> mAddContactPresenter;

    private ProfileDialog mProfileDialog;

    private RecyclerView.LayoutManager mLayoutManager;
    private ContactsAdapter mContactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mAddContactPresenter.onAttach(this);
        ButterKnife.bind(this);

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
    public void setupRecyclerView(List<User> unFriends) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContactsAdapter = new ContactsAdapter(unFriends, this, this);
        mAddContactRecyclerView.setLayoutManager(mLayoutManager);
        mAddContactRecyclerView.setAdapter(mContactsAdapter);
    }

    @Override
    public void onClick(View view, int position, String id) {
        mAddContactPresenter.onItemClicked(view, position, id);
    }

    @Override
    public void showProfileDialog(String imagePath, String username, String mobileNumber) {
        mProfileDialog = new ProfileDialog(this, imagePath, username, mobileNumber,
                this, Constants.DIALOG_ADD_ICON);
        mProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProfileDialog.setCancelable(true);
        mProfileDialog.show();
    }

    @Override
    public void changeDialogIcon() {
        mProfileDialog.changeIcon();
    }

    @Override
    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        mAddContactPresenter.onProfileAddRemoveClick(icon, mobileNumber);
    }

    @Override
    public void updateList() {
        mContactsAdapter.notifyDataSetChanged();
    }
}
