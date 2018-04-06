package com.im.smarto.ui.contacts;

import com.im.smarto.ui.ILoading;
import com.im.smarto.ui.base.BaseView;
import com.im.smarto.db.entities.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface IContactsFragment extends BaseView, ILoading, ContactItemClickListener {

    void setupRecyclerView();
    void startAddActivity();
    void updateList();
    void updateData(List<User> friendList);

    void showProfileDialog(int id, String imagePath, String username, String mobileNumber, int trustId);
    void changeDialogIcon();
    void deleteContact(String mobileNumber);
    void addContact(String name, String mobileNumber);

    void dismissDialog();

    void showNetworkError();
}
