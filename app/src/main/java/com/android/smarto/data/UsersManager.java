package com.android.smarto.data;

import android.util.Log;

import com.android.smarto.db.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

@Singleton
public class UsersManager {

    public static final String TAG = UsersManager.class.getSimpleName();

    public List<User> usersList;
    public List<User> friendsList;
    public List<User> notFriends;
    public User mCurrentUser;

    public UsersManager(){
        this.usersList = new ArrayList<>();
        this.notFriends = null;
        mCurrentUser = new User();
        friendsList = null;
        initData();

    }

    private void initData() {
        User user = new User();
        user.setUniqueId("1");
        user.setFirstName("Sophie");
        user.setLastName("Turner");
        user.setPassword("qwerty");
        user.setMobileNumber("+79867250805");
        user.setProfileImagePath("https://i.imgur.com/jFTXiXb.jpg");
        user.setLatitude(56.321953);
        user.setLongitude(44.032514);
        usersList.add(user);

        User user1 = new User();
        user1.setUniqueId("2");
        user1.setFirstName("Hannah");
        user1.setLastName("Rose");
        user1.setPassword("qwerty");
        user1.setMobileNumber("+79159336790");
        user1.setProfileImagePath("https://i.imgur.com/07ALk21.jpg");
        user1.setLatitude(56.323036);
        user1.setLongitude(44.034145);
        usersList.add(user1);

        User user2 = new User();
        user2.setUniqueId("3");
        user2.setFirstName("Sam");
        user2.setLastName("Brown");
        user2.setPassword("qwerty");
        user2.setMobileNumber("+79026575817");
        user2.setProfileImagePath("https://i.imgur.com/gAyMyWD.jpg");
        user2.setLatitude(56.322078);
        user2.setLongitude(44.037879);
        usersList.add(user2);

        User user3 = new User();
        user3.setUniqueId("4");
        user3.setFirstName("Eik");
        user3.setLastName("en Zwijn");
        user3.setPassword("qwerty");
        user3.setMobileNumber("+79016575817");
        user3.setProfileImagePath("https://i.imgur.com/4dAES14.jpg");
        user3.setLatitude(56.320107);
        user3.setLongitude(44.036224);
        usersList.add(user3);

        User user4 = new User();
        user4.setUniqueId("5");
        user4.setFirstName("Nicola");
        user4.setLastName("Peltz");
        user4.setPassword("qwerty");
        user4.setMobileNumber("+79200393767");
        user4.setProfileImagePath("https://i.imgur.com/zPpJzBp.jpg");
        user4.setLatitude(56.320381);
        user4.setLongitude(44.029315);
        usersList.add(user4);

//        User user5 = new User();
//        user4.setUniqueId("6");
//        user4.setFirstName("Emma");
//        user4.setLastName("Roberts");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79046575817");
//        user4.setProfileImagePath("https://i.imgur.com/7stzxMP.jpg");
//        usersList.add(user5);

//        User user6 = new User();
//        user4.setUniqueId("7");
//        user4.setFirstName("Kristen");
//        user4.setLastName("Stewart");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79056575817");
//        user4.setProfileImagePath("https://i.imgur.com/9lCv7nU.jpg");
//        usersList.add(user6);
//
//        User user7 = new User();
//        user4.setUniqueId("8");
//        user4.setFirstName("Barbara");
//        user4.setLastName("Palvin");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79108729605");
//        user4.setProfileImagePath("https://i.imgur.com/ACB6Grf.jpg");
//        usersList.add(user7);

//        User user8 = new User();
//        user4.setUniqueId("9");
//        user4.setFirstName("Robert");
//        user4.setLastName("Redfield");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79308048020");
//        user4.setProfileImagePath("https://i.imgur.com/3fPrpKw.jpg");
//        usersList.add(user8);
//
//        User user9 = new User();
//        user4.setUniqueId("10");
//        user4.setFirstName("Rachel");
//        user4.setLastName("Cook");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79101299837");
//        user4.setProfileImagePath("https://i.imgur.com/Pbsijz4.jpg");
//        usersList.add(user9);
//
//        User user10 = new User();
//        user4.setUniqueId("11");
//        user4.setFirstName("Victoria");
//        user4.setLastName("Justice");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79308016303");
//        user4.setProfileImagePath("https://i.imgur.com/d00wbXs.jpg");
//        usersList.add(user10);

//        User user11 = new User();
//        user4.setUniqueId("12");
//        user4.setFirstName("Sophie");
//        user4.setLastName("Muze");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79066575817");
//        user4.setProfileImagePath("https://i.imgur.com/qST9jV1.jpg");
//        usersList.add(user11);
//
//        User user12 = new User();
//        user4.setUniqueId("13");
//        user4.setFirstName("Isabella");
//        user4.setLastName("Fernandez");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79076575817");
//        user4.setProfileImagePath("https://i.imgur.com/HQg2B9v.jpg");
//        usersList.add(user12);
//
//        User user13 = new User();
//        user4.setUniqueId("14");
//        user4.setFirstName("Martin");
//        user4.setLastName("Coldberg");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79086575817");
//        user4.setProfileImagePath("https://i.imgur.com/MnEKKD2.jpg");
//        usersList.add(user13);
//
//        User user14 = new User();
//        user4.setUniqueId("15");
//        user4.setFirstName("Galina");
//        user4.setLastName("Dubenenko");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79096575817");
//        user4.setProfileImagePath("https://i.imgur.com/q3YF5EX.jpg");
//        usersList.add(user14);
//
//        User user15 = new User();
//        user4.setUniqueId("16");
//        user4.setFirstName("Nikki");
//        user4.setLastName("Sia");
//        user4.setPassword("qwerty");
//        user4.setMobileNumber("+79106575817");
//        user4.setProfileImagePath("https://i.imgur.com/SZUOSlz.jpg");
//        usersList.add(user15);
    }

    public User getUserByQuery(String query){
        for (User user: usersList){
            if (user.getMobileNumber().equals(query) || user.getUniqueId().equals(query)) {
                Log.i(TAG, user.getUniqueId() + " " + user.getFirstName());
                return user;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    public List<User> getNotFriends() {
        return notFriends;
    }

    public void setNotFriends(List<User> notFriends) {
        this.notFriends = notFriends;
    }
}
