package com.android.smarto.db;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.smarto.data.TaskManager;
import com.android.smarto.data.Contact;
import com.android.smarto.data.UsersManager;
import com.android.smarto.db.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

@Singleton
public class DbHelper implements IDbHelper {

    public static final String TAG = DbHelper.class.getSimpleName();

    private UsersManager mUsersManager;
    private TaskManager mTaskManager;
    private Context mContext;

    @Inject
    public DbHelper(UsersManager usersManager, TaskManager taskManager, Context context) {
        this.mUsersManager = usersManager;
        this.mTaskManager = taskManager;
        this.mContext = context;
    }


    @Override
    public TaskManager getTaskManager() {
        return mTaskManager;
    }

    @Override
    public User getUser(String query) {
        return mUsersManager.getUserByQuery(query);
    }

    @Override
    public List<User> getAllUsers() {
        return mUsersManager.usersList;
    }

    @Override
    public void addUser(User user) {
        mUsersManager.usersList.add(user);
    }

    @Override
    public User getCurrentUser() {
        return mUsersManager.getCurrentUser();
    }

    @Override
    public void setCurrentUser(User user) {
        mUsersManager.setCurrentUser(user);
    }

    @Override
    public void removeFriend(User user) {
        mUsersManager.notFriends.add(user);
        mUsersManager.friendsList.remove(user);
        //deleteContact(mContext.getContentResolver(), user.mobileNumber);
    }

    private void deleteContact(ContentResolver contentResolver, String number) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String[] args = new String[] { String.valueOf(getContactID(contentResolver, number)) };
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private long getContactID(ContentResolver contentResolver, String number) {

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = { ContactsContract.PhoneLookup._ID };
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(contactUri, projection, null, null,null);
            if (cursor.moveToFirst()) {
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return -1;

    }

    @Override
    public void addFriend(User user) {
        mUsersManager.notFriends.remove(user);
        mUsersManager.friendsList.add(user);
        //insertContact(mContext.getContentResolver(), user.getName(), user.getMobileNumber());
    }

    private boolean insertContact(ContentResolver contentResolver, String name, String mobileNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> getFriendList() {

        if (mUsersManager.getFriendsList() == null) {

            List<Contact> friends = new ArrayList<>();
            String name = "";
            String phone = "";

            ContentResolver cr = mContext.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phone = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (phone.startsWith("8")) {
                                phone = "+7" + phone.substring(1);
                            }

                            phone = phone.replaceAll(" -", "");

                            Log.i(TAG, "Name: " + name);
                            Log.i(TAG, "Phone Number: " + phone);
                        }
                        pCur.close();
                    }
                    friends.add(new Contact(friends.size(), name, phone));
                }
            }
            if (cur != null) {
                cur.close();
            }

            List<User> users = mUsersManager.usersList;
            List<User> myFriends = new ArrayList<>();

            Log.i(TAG, users.toString() + "\n" + friends.toString());

            for (User user : users) {
                if (!user.equals(mUsersManager.getCurrentUser())) {
                    for (Contact contact : friends) {
                        Log.i(TAG, "azaza: " + user.getUniqueId() + " " + user.getName());
                        if (user.getMobileNumber().equals(contact.getMobileNumber())) {
                            Log.i(TAG, user.getUniqueId() + " " + user.getName());
                            myFriends.add(user);
                        }
                    }
                }
            }

            List<User> unFriend = new ArrayList<>();

            for (User user : users) {
                if (user.uniqueId != mUsersManager.getCurrentUser().uniqueId) {
                    if (!myFriends.contains(user)) {
                        unFriend.add(user);
                    }
                }
            }

            mUsersManager.setNotFriends(unFriend);

            mUsersManager.setFriendsList(myFriends);
        }

        return mUsersManager.getFriendsList();
    }

    @Override
    public List<User> getUnfriends() {
        return mUsersManager.getNotFriends();
    }

    @Override
    public List<User> getSortedUnFriends(String query) {

        List<User> unFriends = mUsersManager.notFriends;
        List<User> sortedList = new ArrayList<>();

        for (User user: unFriends){
            if (user.getName().toLowerCase().replaceAll(" ","").contains(query.toLowerCase())
                    || user.getMobileNumber().toLowerCase().replaceAll(" ","").contains(query.toLowerCase())) {
                Log.i(TAG, user.getName().toLowerCase() + " - " + query.toLowerCase());
                sortedList.add(user);
            }
        }

        return sortedList;
    }

}
