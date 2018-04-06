package com.im.smarto;

/**
 * Created by Anatoly Chernyshev on 26.01.18.
 */

public class Constants {

    public static final String NETWORK_ERROR = "Unable to resolve host "
            + "\"ec2-52-14-6-55.us-east-2.compute.amazonaws.com\": No address associated with hostname";

    public static final String CURRENT_USER_FIRST_NAME = "current_user_first_name";
    public static final String CURRENT_USER_LAST_NAME = "current_user_last_name";
    public static final String CURRENT_USER_PHONE = "current_user_phone";
    public static final String CURRENT_USER_ID = "current_user_id";
    public static final String CURRENT_USER_IMG_LINK = "current_user_img_link";

    public static final String USER_TOKEN = "user_token";
    public static final String REMEMBER_USER = "is_remember_user";
    public static final String TEST_USERNAME = "smarto";
    public static final String TEST_PASSWORD = "password";
    public static final String BASE_API_URL = "http://ec2-52-14-6-55.us-east-2.compute.amazonaws.com:8080/rest/";
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    public static final String TIME_PICKER_DIALOG_TAG = "Timepickerdialog";
    public static final String DATE_PICKER_DIALOG_TAG = "Datepickerdialog";
    public static final String EDIT_PROFILE_TYPE = "Edit_profile_type";

    public static final int FIRST_NAME_TYPE = 1;
    public static final int LAST_NAME_TYPE = 2;
    public static final int PHONE_TYPE = 3;

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";

    public static final int DESCRIPTION = 1;
    public static final int DATE = 2;

    public static final String CURRENT_LOCATION = "current_location";

    public static final String GROUP_POSITION = "group_position";

    public static final String DIALOG_DELETE_ICON = "delete";
    public static final String DIALOG_ADD_ICON = "add";

    public static final String COMMON_TASK = "Common";
    public static final String MEETING_TASK = "Meeting";
    public static final String GOODS_TASK = "Goods";

    public static final int COMMON_TASK_TYPE = 1;
    public static final int MEETING_TASK_TYPE = 2;
    public static final int GOODS_TASK_TYPE = 3;

    public static final String CHILD_ITEM_ID = "child_id";
    public static final String GROUP_ITEM_ID = "group_id";
    public static final String CONTACT_ID = "contact_id";
    public static final String DIALOG_FROM_CONTACTS = "dialog_from_contacts";

    public class Database {

        public static final String USER_TABLE_NAME = "user";

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
