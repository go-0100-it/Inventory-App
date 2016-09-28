package com.waters89gmail.dave.totalinventorycontrol.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WatersD on 8/11/2016.
 */
public class DataBaseProvider extends ContentProvider {
    public static final String LOG_TAG = ("MY_LOG");

    static final String AUTHORITY = "com.waters89gmail.dave.database.database_provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String SINGLE_ROW_MIME = "vnd.android.cursor.item/vnd.com.waters89gmail.dave.totalinventorycontrol.database.databaseprovider.database_name";
    static final String MULTI_ROW_MIME = "vnd.android.cursor.dir/vnd.com.waters89gmail.dave.totalinventorycontrol.database.databaseprovider.database_name";

    DataBaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        Log.d(LOG_TAG,"ContentProvider:OnCreate");
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        if(uri.getLastPathSegment().equals(null)){
            return MULTI_ROW_MIME;
        }else {
            return SINGLE_ROW_MIME;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String isNullable = DataBaseContract.ProductsTable.DESCRIPTION;
        String table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
        // Insert the new row
        long id = dbHelper.getWritableDatabase().insert(table, isNullable, values);
        if(id != -1) {
            uri = Uri.withAppendedPath(uri, String.valueOf(id));
        }
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
        dbHelper.getWritableDatabase().update(table,values,selection,selectionArgs);
        return 0;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
        Cursor cursor = dbHelper.getWritableDatabase().query(table, projection, selection, selectionArgs, null, null, null);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public class DataBaseHelper extends SQLiteOpenHelper {

        /**
         * Constructor is private to prevent direct instantiation.
         * to instantiate make call to static method "getInstance()" instead.
         */
        private DataBaseHelper(Context context) {
            super(context, DataBaseContract.DATABASE_NAME, null, DataBaseContract.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DataBaseContract.SQL_CREATE_PRODUCTS_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_BUSINESS_AGENTS_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_ORDERING_DETAILS_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_TRANSACTION_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_SALES_KEY_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_PRODUCT_ORDERING_DETAILS_KEY_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_PURCHASES_KEY_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_PRODUCT_AGENT_KEY_TABLE);
            db.execSQL(DataBaseContract.SQL_CREATE_PRODUCT_CATEGORY_KEY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }


}
