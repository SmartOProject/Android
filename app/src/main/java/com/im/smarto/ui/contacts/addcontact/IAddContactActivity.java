package com.im.smarto.ui.contacts.addcontact;

import com.im.smarto.ui.ILoading;
import com.im.smarto.ui.base.BaseView;
import com.im.smarto.ui.contacts.ContactItemClickListener;
import com.im.smarto.db.entities.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 23.02.2018.
 */

public interface IAddContactActivity extends BaseView, ILoading, ContactItemClickListener {

    void setupRecyclerView();
    void showProfileDialog(int id, String imagePath, String username, String mobileNumber, String icon);
    void changeDialogIcon();
    void hideContactHelper();
    void updateData(List<User> data);
    void showContactAddedSnackBar();
    void hideKeyboard();

}
