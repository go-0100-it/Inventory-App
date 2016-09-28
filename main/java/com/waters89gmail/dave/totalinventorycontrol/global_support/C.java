package com.waters89gmail.dave.totalinventorycontrol.global_support;

import android.Manifest;

/**
 * A Class to define Global Constants
 */
public abstract class C {

    // Storage Permissions
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String STORAGE_PERMISSIONS_DIALOG = "storage_permissions_dialog";

    // Phone Permissions
    public static String[] CALL_PERMISSIONS = {Manifest.permission.CALL_PHONE};
    public static final int REQUEST_CALL_PERMISSIONS = 2;
    public static final String PHONE_PERMISSIONS_DIALOG = "phone_permissions_dialog";

    // Camera Permissions
    public static String[] CAMERA_PERMISSIONS = {Manifest.permission.CAMERA};
    public static final int REQUEST_CAMERA_PERMISSIONS = 3;
    public static final String CAMERA_PERMISSIONS_DIALOG = "camera_permissions_dialog";

    public static final int PRODUCT_LIST_LOADER = 1;
    public static final int BUSINESS_AGENT_LIST_LOADER = 2;
    public static final int ASSOCIATE_AGENT_DIALOG_LOADER = 3;

    //Commonly used values and strings.
    public static final String LOG_TAG = "MY_LOG";
    public static final String EMPTY_STRING = "";
    public static final String UNDER_SCORE = "_";
    public static final String SPACE = " ";
    public static final String DOT_JPG = ".jpg";
    public static final String JPEG = "JPEG";
    public static final String IMAGE_TYPE = "image/*";
    public static final int NULL = 0;
    public static final int NO_RESULT = -1;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    //Agent Activity or Object related constants
    public static final String PRODUCT = "product";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_QTY = "product_qty";
    public static final String EDIT_PRODUCT = "edit_product";
    public static final String PRODUCT_FOR_ASSOCIATION = "product_for_association";
    public static final String ASSOCIATE_AGENT_DIALOG = "associate_agent_dialog";

    //Agent Activity or Object related constants
    public static final String AGENT = "agent";
    public static final String AGENT_TO_VIEW = "agent_to_view";
    public static final String AGENT_TO_EDIT = "agent_to_edit";
    public static final String EDITED_AGENT = "editted_agent";
    public static final String EDIT_AGENT = "edit_agent";
    public static final String AGENT_ID = "agent_id";
    public static final String PRODUCT_SAVED_INSTANCE = "product_saved_instance";
    public static final String AGENT_SAVED_INSTANT = "agent_saved_instance";

    //Constants used for intents.
    public static final int CAMERA_REQUEST = 900;
    public static final int GALLERY_REQUEST = 800;
    public static final int ADD_PRODUCT_REQUEST_CODE = 100;
    public static final int ADD_AGENT_REQUEST_CODE = 200;
    public static final int EDIT_PRODUCT_REQUEST_CODE = 400;
    public static final int EDIT_AGENT_REQUEST_CODE = 500;
    public static final int PRODUCT_FRAG = 10;
    public static final int AGENT_FRAG = 20;
    public static final int TRANS_FRAG = 30;//for future functionality
    public static final int ASSOCIATE_PRODUCT_REQUEST = 45;
    public static final String FROM_ACTIVITY = "from_activity";
    public static final String TITLE = "title";
    public static final String CURRENT_FRAG = "current_frag";
    public static final String ACTION = "Action";
    public static final String EMAIL_URI_START = "mailto:";
    public static final String PHONE_URI_START = "tel:";
    public static final String DELETED_TYPE = "DELETED_TYPE";
    public static final String COUNT = "COUNT";

    //Custom Dialog/Spinner constants
    public static final String MESSAGE = "message";
    public static final String POSITIVE_BTN = "positive_btn";
    public static final String NEUTRAL_BTN = "neutral_btn";
    public static final String NEGATIVE_BTN = "negative_btn";
    public static final String IMAGE_DIALOG_TAG = "image_dialog";
    public static final String EDIT_INVENTORY_DIALOG_TAG = "edit_inventory_dialog";
    public static final String ADD_SUPPLIER_DIALOG_TAG = "add_supplier_dialog";
    public static final String EDIT_SUPPLIER_DIALOG_TAG = "edit_supplier_dialog";
    public static final String DELETE_PRODUCT_DIALOG_TAG = "delete_product_dialog";
    public static final String DELETE_AGENT_DIALOG_TAG = "delete_agent_dialog";

    public C() {}
}
