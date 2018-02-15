package com.android.smarto;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class Constants {

    public static final String USER_UUID = "user_uuid";
    public static final String TEST_USERNAME = "smarto";
    public static final String TEST_PASSWORD = "password";
    public static final String BASE_API_URL = "https://www.google_button_login.ru";
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public class Database {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "usersDb";
        public static final String TABLE_USERS = "users";
        public static final String TABLE_TASKS = "tasks";

        public static final String KEY_ID = "_id";
        public static final String KEY_EMAIL = "mail";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_FIRST_NAME = "first_name";
        public static final String KEY_SECOND_NAME = "second_name";
        public static final String KEY_IMAGE_PATH = "image_path";
        public static final String KEY_LIST = "list";

    }

}
