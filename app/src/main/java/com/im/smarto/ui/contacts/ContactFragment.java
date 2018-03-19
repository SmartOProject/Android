package com.im.smarto.ui.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseFragment;
import com.im.smarto.ui.contacts.addcontact.AddContactActivity;
import com.im.smarto.ui.contacts.dialog.ProfileBtnAddRemoveClick;
import com.im.smarto.ui.contacts.dialog.ProfileDialog;
import com.im.smarto.db.entities.User;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactFragment extends BaseFragment implements IContactsFragment,
        ProfileBtnAddRemoveClick {

    public static final String TAG = ContactFragment.class.getSimpleName();

    @BindView(R.id.contacts_recycler_view) RecyclerView mContactsList;
    @BindView(R.id.normal_progress) ProgressWheel mProgressBar;

    @Inject
    ContactsPresenter<IContactsFragment> mContactsPresenter;

    private List<User> friendsData;

    private RecyclerView.LayoutManager mLayoutManager;
    private ContactsAdapter mContactsAdapter;
    private ProfileDialog mProfileDialog;

    public ContactFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.fab_contacts)
    void onClick(){
        mContactsPresenter.onFABCLicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsData = new ArrayList<>();
        if (getArguments() != null) {
        }
        mContactsPresenter.onAttach(this);

    }

    private void requestContactsPermissions() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS},
                    Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            mContactsPresenter.onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mContactsPresenter.onPermissionsGranted();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, v);
        mContactsPresenter.onCreate();
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        requestContactsPermissions();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContactsPresenter.onDetach();
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
    public void setupRecyclerView() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mContactsAdapter = new ContactsAdapter(friendsData, mContext, this);
        mContactsList.setLayoutManager(mLayoutManager);
        mContactsList.setAdapter(mContactsAdapter);
    }

    @Override
    public void startAddActivity() {
        startActivity(new Intent(mContext, AddContactActivity.class));
    }

    @Override
    public void updateList() {
        mContactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(List<User> friendList) {
        mContactsAdapter.updateList(friendList);
    }

    @Override
    public void onClick(User user){
        mContactsPresenter.onItemClicked(user);
    }

    @Override
    public void showProfileDialog(String imagePath, String username, String mobileNumber) {
        mProfileDialog = new ProfileDialog(mContext, imagePath, username, mobileNumber,
                this, Constants.DIALOG_DELETE_ICON);
        mProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProfileDialog.setCancelable(true);
        mProfileDialog.show();
    }

    @Override
    public void changeDialogIcon() {
        mProfileDialog.changeIcon();
    }

    @Override
    public void deleteContact(String mobileNumber) {

    }

    @Override
    public void addContact(String name, String mobileNumber) {

    }

    @Override
    public void dismissDialog() {
        mProfileDialog.dismiss();
    }

    @Override
    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        mContactsPresenter.onProfileAddRemoveClick(icon, mobileNumber);
    }
}
