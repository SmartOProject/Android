package com.android.smarto.architecture.contacts;

import android.content.Context;
import android.view.View;

import com.android.smarto.architecture.ILoading;
import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.data.Contact;
import com.android.smarto.db.model.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface IContactsFragment extends BaseView, ILoading, ContactItemClickListener {

    void setupRecyclerView(List<User> userList);
    void startAddActivity();
    void updateList();

    void showProfileDialog(String imagePath, String username,
                           String mobileNumber, int imgRes);
    void changeDialogIcon();
    void deleteContact(String mobileNumber);
    void addContact(String name, String mobileNumber);

}
