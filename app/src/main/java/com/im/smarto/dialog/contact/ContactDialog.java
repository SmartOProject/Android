package com.im.smarto.dialog.contact;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.db.entities.User;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anatoly Chernyshev on 04.04.2018.
 */

public class ContactDialog extends DialogFragment implements IContactDialog {

    private static final String TAG = ContactDialog.class.getSimpleName();

    @Inject
    ContactDialogPresenter<IContactDialog> mContactDialogPresenter;

    @BindView(R.id.contact_image) CircleImageView mContactImage;
    @BindView(R.id.contact_first_name) TextView mContactName;
    @BindView(R.id.selected_contact_phone) TextView mContactPhone;
    @BindView(R.id.contact_trust_button) FloatingActionButton mTrustButton;
    @BindView(R.id.contact_delete_button) FloatingActionButton mAddDeleteButton;

    private int mCurrentContactId;
    private boolean mIsDialogFromContacts;
    private int mCurrentTrustId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_dialog, null);
        ButterKnife.bind(this, v);
        AndroidSupportInjection.inject(this);
        mContactDialogPresenter.onAttach(this);

        Bundle args = getArguments();

        if (args != null) {
            mCurrentContactId = args.getInt(Constants.CONTACT_ID);
            mIsDialogFromContacts = args.getBoolean(Constants.DIALOG_FROM_CONTACTS);
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mContactDialogPresenter.onCreateView(mCurrentContactId, mIsDialogFromContacts);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContactDialogPresenter.onDetach();
    }

    @OnClick({R.id.contact_delete_button, R.id.contact_trust_button})
    void onClick(View v) {
        switch(v.getId()) {
            case R.id.contact_delete_button:
                mContactDialogPresenter.onDeleteClicked();
                break;
            case R.id.contact_trust_button:
                mContactDialogPresenter.onTrustClicked(mCurrentTrustId);
                break;
        }
    }

    @Override
    public void initContactViews(User contactById) {

        initImage(contactById.getImgUrl());
        initName(contactById.getFirstName(), contactById.getLastName());
        initPhone(contactById.getPhone());
        if (mIsDialogFromContacts)
            initTrustButton(contactById.getTrustId());

    }

    @Override
    public void changeTrustState() {
        if (mCurrentTrustId == 0) {
            mTrustButton.setColorNormal(getContext()
                    .getResources().getColor(R.color.primary_normal));
            mTrustButton.setImageDrawable(getContext()
                    .getResources().getDrawable(R.drawable.ic_clear_white_24dp));
            mCurrentTrustId = 1;
            Toast.makeText(getActivity(), "This contact is now trusted", Toast.LENGTH_LONG).show();
        } else {
            mTrustButton.setColorNormal(getContext()
                    .getResources().getColor(R.color.white));
            mTrustButton.setImageDrawable(getContext()
                    .getResources().getDrawable(R.drawable.ic_check_blue_24dp));
            mCurrentTrustId = 0;
            Toast.makeText(getActivity(), "This contact is now untrusted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void dismissDialog() {
        getDialog().dismiss();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setupDeleteAndTrustButton() {
        mTrustButton.setEnabled(false);
        mAddDeleteButton.setColorNormal(getContext()
                .getResources().getColor(R.color.white));
        mAddDeleteButton.setImageDrawable(getContext()
                .getResources().getDrawable(R.drawable.ic_person_add_blue_24dp));
    }

    @Override
    public void showUserAddedToast() {
        Toast.makeText(getContext(), "User added!", Toast.LENGTH_LONG).show();
    }

    private void initTrustButton(int trustId) {
        mCurrentTrustId = trustId;
        if (trustId == 0) {
            mTrustButton.setColorNormal(getContext()
                    .getResources().getColor(R.color.white));
            mTrustButton.setImageDrawable(getContext()
                    .getResources().getDrawable(R.drawable.ic_check_blue_24dp));
        } else {
            mTrustButton.setColorNormal(getContext()
                    .getResources().getColor(R.color.primary_normal));
            mTrustButton.setImageDrawable(getContext()
                    .getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        }
    }

    private void initPhone(String phone) {
        mContactPhone.setText(phone);
        mContactPhone.setVisibility(View.VISIBLE);
    }

    private void initName(String firstName, String lastName) {
        String name = firstName + " " + lastName;
        mContactName.setText(name);
        mContactName.setVisibility(View.VISIBLE);
    }

    private void initImage(String imgUrl) {
        Picasso.with(getContext()).load(imgUrl)
                .resize(200, 200)
                .placeholder(R.drawable.profile_image)
                .centerCrop().into(mContactImage);
    }
}
