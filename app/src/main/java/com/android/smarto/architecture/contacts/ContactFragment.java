package com.android.smarto.architecture.contacts;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseFragment;
import com.android.smarto.architecture.contacts.addcontact.AddContactActivity;
import com.android.smarto.architecture.contacts.dialog.ProfileBtnAddRemoveClick;
import com.android.smarto.architecture.contacts.dialog.ProfileDialog;
import com.android.smarto.db.model.User;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.smarto.Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS;

public class ContactFragment extends BaseFragment implements IContactsFragment,
        ProfileBtnAddRemoveClick{

    public static final String TAG = ContactFragment.class.getSimpleName();

    @BindView(R.id.contacts_recycler_view) RecyclerView mContactsList;
    @BindView(R.id.normal_progress) ProgressWheel mProgressBar;

    @Inject
    ContactsPresenter<IContactsFragment> mContactsPresenter;

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
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            mContactsPresenter.onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
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
    public void setupRecyclerView(List<User> userList) {
        Log.i(TAG, userList.toString());
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mContactsAdapter = new ContactsAdapter(userList, mContext, this);
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
    public void onClick(View v, int position, String id){
        mContactsPresenter.onItemClicked(v, position, id);
    }

    @Override
    public void showProfileDialog(String imagePath, String username, String mobileNumber, int imgRes) {
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
    public void onProfileAddRemoveClick(String icon, String mobileNumber) {
        mContactsPresenter.onProfileAddRemoveClick(icon, mobileNumber);
    }
}
