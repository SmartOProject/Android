package com.im.smarto.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseArray;

import com.im.smarto.db.entities.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

@Singleton
public class UsersManager {

    public static final String TAG = UsersManager.class.getSimpleName();

    private User mCurrentUser;
    private Context mContext;
    private SparseArray<String> mContactsMap;
    private List<User> mContacts;
    private List<User> mSearchUserList;
    private ContactDataChangesListener mListener;

    @Inject
    public UsersManager(Context context){
        this.mContext = context;
    }

    public void setContactDataChangedListener(ContactDataChangesListener listener){
        this.mListener = listener;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    public List<String> getContactPhones(){
        List<String> phoneList = new ArrayList<>();
        Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replace(" ","");
            phoneNumber = phoneNumber.replace("-","");
            if (phoneNumber.startsWith("8")) {
                phoneNumber = "+7" + phoneNumber.substring(1);
            }
            Log.i(TAG, "Name: " + name + "\n" + "Phone: " + phoneNumber);
            if (phoneNumber.length() > 11) phoneList.add(phoneNumber);
        }
        phones.close();
        Log.i(TAG, phoneList.toString());
        return phoneList;
    }

    public String[] getContactNames(){
        if (mContactsMap == null) return null;
        List<String> arrayList = new ArrayList<>(mContactsMap.size());
        for (int i = 0; i < mContactsMap.size(); i++)
            arrayList.add(mContactsMap.valueAt(i));
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public int getId(String name){
        int position = mContactsMap.indexOfValue(name);
        return mContactsMap.keyAt(position);
    }

    public void updateContactsMap(List<User> users){
        mContacts = users;
        Log.i(TAG, "updateContactsMap() " + users.toString());
        mContactsMap = new SparseArray<>(users.size());

        for (User user: users) {
            mContactsMap.append(user.getId(), user.getName());
        }

    }

    public void deleteFromContactMap(int id) {
        Log.i(TAG, "deleteFromContactMap() " + String.valueOf(id));

        mContactsMap.delete(id);
    }

    public void insertIntoContactMap(int id, String name) {
        Log.i(TAG, "insertIntoContactMap() " + String.valueOf(id) + " " + String.valueOf(name));

        mContactsMap.append(id, name);
    }

    public List<User> getContacts() {
        return mContacts;
    }

    public void setContacts(List<User> contacts) {
        mContacts = contacts;
    }

    public User getContactById(int id) {

        User contact = null;

        for (User user: mContacts)
            if (user.getId() == id) contact = user;

        return contact;
    }

    public void deleteContact(User contact){
        mContacts.remove(contact);
        mListener.onContactDataChanged();
    }

    public void insertContact(User contact){
        mContacts.add(contact);
        mListener.onContactDataChanged();
    }

    public List<User> getSearchUserList() {
        return mSearchUserList;
    }

    public void setSearchUserList(List<User> searchUserList) {
        mSearchUserList = searchUserList;
    }

    public User getUserById(int currentContactId) {
        User contact = null;

        for (User user: mSearchUserList)
            if (user.getId() == currentContactId) contact = user;

        return contact;
    }
}
