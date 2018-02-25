package com.android.smarto.architecture.contacts.addcontact;

import com.android.smarto.architecture.ILoading;
import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.architecture.contacts.ContactItemClickListener;
import com.android.smarto.db.model.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public interface IAddContactActivity extends BaseView, ILoading, ContactItemClickListener{

    void setupRecyclerView(List<User> unFriends);
    void showProfileDialog(String imagePath, String username, String mobileNumber);
    void changeDialogIcon();
    void updateList();

}
