package com.waters89gmail.dave.totalinventorycontrol.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

public class DataBaseProvider extends ContentProvider {

    // Defining Uri constants
    private static final String AUTHORITY = "com.waters89gmail.dave.database.database_provider";
    public static final Uri PRODUCTS_URI = Uri.parse("content://" + AUTHORITY + "/" + DataBaseContract.ProductsTable.TABLE_PRODUCTS);
    public static final Uri AGENTS_URI = Uri.parse("content://" + AUTHORITY + "/" + DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS);
    public static final Uri PRODUCT_AGENTS_KEY_URI = Uri.parse("content://" + AUTHORITY + "/" + DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY);
    private static final String SINGLE_ROW_MIME = "vnd.android.cursor.item/vnd.com.waters89gmail.dave.totalinventorycontrol.database.databaseprovider.database_name";
    private static final String MULTI_ROW_MIME = "vnd.android.cursor.dir/vnd.com.waters89gmail.dave.totalinventorycontrol.database.databaseprovider.database_name";

    DataBaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        if(uri.getLastPathSegment().equals(C.EMPTY_STRING)){
            return MULTI_ROW_MIME;
        }else {
            return SINGLE_ROW_MIME;
        }
    }

    /**
     * Overriding this method to handle requests to insert a new row.  Calling this method from multiple Activities.
     * @param uri The content:// URI of the insertion request. Needs to include table name as the last segment.
     * @param values the set of column_name/value pairs to add to the database.
     * @return The URI for the newly inserted item. The last segment is the row _ID.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        String isNullable;
        String table;

        switch (uri.getLastPathSegment()){
            case DataBaseContract.ProductsTable.TABLE_PRODUCTS:
                isNullable = DataBaseContract.ProductsTable.DESCRIPTION;
                table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
                break;
            case DataBaseContract.TransactionTable.TABLE_TRANSACTIONS:
                isNullable = DataBaseContract.TransactionTable.TRANS_METHOD;
                table = DataBaseContract.TransactionTable.TABLE_TRANSACTIONS;
                break;
            case DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS:
                isNullable = DataBaseContract.BusinessAgentsTable.COMPANY_NAME;
                table = DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS;
                break;
            case DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY:
                isNullable = DataBaseContract.ProductAgentKeysTable.AGENT_KEY;
                table = DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY;
                break;
            case DataBaseContract.SalesKeyTable.TABLE_SALES_KEY:
                isNullable = DataBaseContract.SalesKeyTable.AGENT_KEY;
                table = DataBaseContract.SalesKeyTable.TABLE_SALES_KEY;
                break;
            default:
                isNullable = null;
                table = null;
        }
        // Insert the new row
        long id = dbHelper.getWritableDatabase().insert(table, isNullable, values);
        if(id != C.NO_RESULT) {
            uri = Uri.withAppendedPath(uri, String.valueOf(id));
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    /**
     * Overriding this method to handle requests to update one or more rows.  Calling this method from multiple Activities.
     * @param uri  The content:// URI of the update request. Needs to include table name as the last segment.
     * @param values A set of column_name/value pairs to update in the database.
     * @param selection The column name to search for the selectionArgs.
     * @param selectionArgs the values to search for in the given selection.
     * @return the number of rows affected.
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String table;
        switch (uri.getLastPathSegment()){
            case DataBaseContract.ProductsTable.TABLE_PRODUCTS:
                table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
                break;
            case DataBaseContract.TransactionTable.TABLE_TRANSACTIONS:
                table = DataBaseContract.TransactionTable.TABLE_TRANSACTIONS;
                break;
            case DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY:
                table = DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY;
                break;
            case DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS:
                table = DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS;
                break;
            default:
                table = null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return dbHelper.getWritableDatabase().update(table,values,selection,selectionArgs);
    }

    /**
     * Overriding this method to handle query requests.  Calling this method from multiple Activities.
     * @param uri   The content:// URI of the query request. Needs to include table name as the last segment.
     * @param projection The columns to return.
     * @param selection The column name to search for the selectionArgs.
     * @param selectionArgs the values to search for in the given selection.
     * @param sortOrder optional Arg to specify how the data in the cursor will be sorted.
     * @return a cursor with query results or null.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table;
        Cursor cursor;

        switch (uri.getLastPathSegment()){
            case DataBaseContract.ProductsTable.TABLE_PRODUCTS:
                table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
                break;
            case DataBaseContract.TransactionTable.TABLE_TRANSACTIONS:
                table = DataBaseContract.TransactionTable.TABLE_TRANSACTIONS;
                break;
            case DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY:
                table = DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY;
                break;
            case DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS:
                table = DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS;
                break;
            default:
                table = null;
        }

        cursor = dbHelper.getWritableDatabase().query(table, projection, selection, selectionArgs, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    /**
     * Overriding this method to handle requests to delete one or more rows.  Calling this method from multiple Activities.
     * @param uri  The content:// URI of the update request. Needs to include table name as the last segment.
     * @param selection The column name to search for the selectionArgs.
     * @param selectionArgs the values to search for in the given selection.
     * @return the number of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        String table;
        switch (uri.getLastPathSegment()) {
            case DataBaseContract.ProductsTable.TABLE_PRODUCTS:
                table = DataBaseContract.ProductsTable.TABLE_PRODUCTS;
                break;
            case DataBaseContract.TransactionTable.TABLE_TRANSACTIONS:
                table = DataBaseContract.TransactionTable.TABLE_TRANSACTIONS;
                break;
            case DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY:
                table = DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY;
                break;
            case DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS:
                table = DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS;
                break;
            default:
                table = null;
        }
        return dbHelper.getWritableDatabase().delete(table, selection, selectionArgs);
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
