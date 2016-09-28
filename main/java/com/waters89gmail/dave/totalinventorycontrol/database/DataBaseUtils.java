package com.waters89gmail.dave.totalinventorycontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.waters89gmail.dave.totalinventorycontrol.agent_activity.BusinessAgent;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.Product;

public class DataBaseUtils {

    /**
     * A method to return a BusinessAgent Object stored in the DataBase. This method exists to reduce multiple occurrences of common code.
     * @param context require context to get ContentResolver
     * @param agentKey The _ID of the Agent to be gotten...
     * @return Returns a BusinessAgent Object created form the data queried from the DataBase.
     */
    public static BusinessAgent getAgent(Context context, long agentKey) {

        Cursor cursor;
        String[] projection = new String[]{
                DataBaseContract.BusinessAgentsTable.AGENT_NAME,
                DataBaseContract.BusinessAgentsTable.ADDRESS_1,
                DataBaseContract.BusinessAgentsTable.ADDRESS_2,
                DataBaseContract.BusinessAgentsTable.EMAIL,
                DataBaseContract.BusinessAgentsTable.CONTACT_1,
                DataBaseContract.BusinessAgentsTable.CONTACT_2,
                DataBaseContract.BusinessAgentsTable.PREFERRED_CONTACT,
                DataBaseContract.BusinessAgentsTable.WEBSITE,
                DataBaseContract.BusinessAgentsTable.COMPANY_NAME};
        String selection = DataBaseContract.BusinessAgentsTable._ID + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = new String[]{String.valueOf(agentKey)};

        cursor = context.getContentResolver().query(DataBaseProvider.AGENTS_URI, projection, selection, selectionArgs, null);

        BusinessAgent agent = null;
        if (cursor != null) {
            cursor.moveToFirst();
            agent = new BusinessAgent(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
            cursor.close();
        }
        return agent;
    }

    /**
     * A method to return the _ID of a BusinessAgent Object associated with a Product Object. This method exists to reduce multiple occurrences of common code.
     * @param context require context to get ContentResolver
     * @param productId the _ID of the product to search for in the table.  If it exists then an association has previously been made.
     * @return Returns the _ID of the BusinessAgent associated to the product searched.
     */
    public static long checkForAgent(Context context, long productId) {

        String[] projection = new String[]{DataBaseContract.ProductAgentKeysTable.AGENT_KEY};
        String selection = DataBaseContract.ProductAgentKeysTable.PROD_KEY + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = new String[]{String.valueOf(productId)};

        Cursor cursor = context.getContentResolver().query(DataBaseProvider.PRODUCT_AGENTS_KEY_URI, projection, selection, selectionArgs, null);

        long agentKey = C.NO_RESULT;
        if (cursor != null && cursor.getCount() != C.NULL) {
            cursor.moveToFirst();
            agentKey = cursor.getLong(0);
            cursor.close();
        }
        return agentKey;
    }

    /**
     * A method to create ContentValues of all columns in the ProductsTable.
     * Using this method to insert a new row into Products Table.  Could also be used to update all column fields in a row.
     * @param product all product fields are extracted from the PRODUCT object and put into ContentValues
     * @return returns a ContentValue object only useful with the Products Table.
     */
    public static ContentValues createProductContentValues(Product product){
        // Creating a new ContentValues Object and putting values where column names are the keys
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ProductsTable.NAME, product.getmName());
        cv.put(DataBaseContract.ProductsTable.NUMBER, product.getmNumber());
        cv.put(DataBaseContract.ProductsTable.DESCRIPTION, product.getmDescription());
        cv.put(DataBaseContract.ProductsTable.CATEGORY, product.getmCategory());
        cv.put(DataBaseContract.ProductsTable.PRICE, product.getmPrice());
        cv.put(DataBaseContract.ProductsTable.QTY_ON_HAND, product.getmQtyOnHand());
        cv.put(DataBaseContract.ProductsTable.TYPE, product.getmType());
        cv.put(DataBaseContract.ProductsTable.IMAGE_SOURCE, product.getmImage());
        return cv;
    }

    /**
     *  A method to create ContentValues of all columns in the BusinessAgentsTable.
     *  Using this method to insert a new row into Agents Table.  Could also be used to update all fields in a row.
     * @param agent all agent fields are extracted from the AGENT object and put into ContentValues as Key value pairs.
     * @return returns a ContentValue object only useful with the Agents Table.
     */
    public static ContentValues createAgentContentValues(BusinessAgent agent){
        // Creating a new ContentValues Object and putting values where column names are the keys
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.BusinessAgentsTable.AGENT_NAME, agent.getmAgentName());
        cv.put(DataBaseContract.BusinessAgentsTable.ADDRESS_1, agent.getmAddress1());
        cv.put(DataBaseContract.BusinessAgentsTable.ADDRESS_2, agent.getmAddress2());
        cv.put(DataBaseContract.BusinessAgentsTable.EMAIL, agent.getmEmail());
        cv.put(DataBaseContract.BusinessAgentsTable.CONTACT_1, agent.getmContact1());
        cv.put(DataBaseContract.BusinessAgentsTable.CONTACT_2, agent.getmContact2());
        cv.put(DataBaseContract.BusinessAgentsTable.PREFERRED_CONTACT, agent.getmPreferredContact());
        cv.put(DataBaseContract.BusinessAgentsTable.WEBSITE, agent.getmWebSite());
        cv.put(DataBaseContract.BusinessAgentsTable.COMPANY_NAME, agent.getmCompanyName());
        return cv;
    }

    /**
     * Enters productId and agentId into ProductAgentKeysTable of the App DataBase as key value pairs. This is used for associating a agent as the supplier of a product.
     * Entering the productId and agentId as key value pairs allows for multiple associations for both agent and product.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param productId Product Id of the product to be associated.
     * @param agentId Agent Id of the agent to be associated to the product.
     * @return returns boolean value of true if the database insert was successful and false if unsuccessful.
     */
    public static boolean associateAgentToProduct(Context context, long productId, long agentId){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ProductAgentKeysTable.AGENT_KEY, agentId);
        cv.put(DataBaseContract.ProductAgentKeysTable.PROD_KEY, productId);

        return context.getContentResolver().insert(DataBaseProvider.PRODUCT_AGENTS_KEY_URI, cv) != null;
    }

    /**
     * This method searches the ProductAgentKeysTable by productId and deletes any row containing this productId.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param productId ProductId is the Id of the product to be referenced.  All instances of this productId will be deleted from the Table.
     * @return returns an integer value representing the number of rows deleted.
     */
    public static int deleteProductAgentAssociation(Context context, long productId){

        String selection = DataBaseContract.ProductAgentKeysTable.PROD_KEY + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = new String[]{String.valueOf(productId)};

        return context.getContentResolver().delete(DataBaseProvider.PRODUCT_AGENTS_KEY_URI,selection,selectionArgs);
    }

    /**
     * This method searches the BusinessAgentsTable by agentId and deletes any row containing that agentId.
     * also, this method searches the ProductAgentKeysTable by agentId and deletes any row containing that agentId.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param agentId Agent Id is the Id of the agent to be deleted.  All instances of this agentId will be deleted from the Tables.
     * @return returns an integer array. Index [0] of the array is the number of rows deleted from the BusinessAgentsTable,
     *          index [1] is the number of rows deleted from the ProductsAgentsKeysTable.
     */
    public static int[] deleteAgent(Context context, long agentId){
        int[] result = {C.NULL,C.NULL};
        String selection;
        String[] selectionArgs;

        selection = DataBaseContract.BusinessAgentsTable._ID + DataBaseContract.WHERE_EQUALS;
        selectionArgs = new String[]{String.valueOf(agentId)};

        result[0] = context.getContentResolver().delete(DataBaseProvider.AGENTS_URI,selection,selectionArgs);

        selection = DataBaseContract.ProductAgentKeysTable.AGENT_KEY + DataBaseContract.WHERE_EQUALS;
        selectionArgs = new String[]{String.valueOf(agentId)};

        result[1] = context.getContentResolver().delete(DataBaseProvider.PRODUCT_AGENTS_KEY_URI,selection,selectionArgs);

        return result;
    }

    /**
     * This method searches the ProductsTable by productId and deletes any row containing that productId.
     * also, this method searches the ProductAgentKeysTable by productId and deletes any row containing that productId.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param productId the Id of the product to be deleted.  All instances of this productId will be deleted from the Tables.
     * @return returns an integer array. Index [0] of the array is the number of rows deleted from the ProductsTable,
     *          index [1] is the number of rows deleted from the ProductsAgentsKeysTable.
     */
    public static int[] deleteProduct(Context context, long productId){
        int[] result = {C.NULL,C.NULL};

        String selection = DataBaseContract.ProductsTable._ID + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = new String[]{String.valueOf(productId)};

        result[0] = context.getContentResolver().delete(DataBaseProvider.PRODUCTS_URI,selection,selectionArgs);
        result[1] = deleteProductAgentAssociation(context,productId);

        return result;
    }

    /**
     * A method to query the database for a specific Products inventory count.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param productId the _ID of the product for deletion.
     * @return returns the value stored for Product Quantity.
     */
    public static int getProductQty(Context context, long productId){

        String[] projection = new String[]{DataBaseContract.ProductsTable.QTY_ON_HAND};
        String selection = DataBaseContract.ProductsTable._ID + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = new String[]{String.valueOf(productId)};

        Cursor cursor = context.getContentResolver().query(DataBaseProvider.PRODUCTS_URI, projection, selection, selectionArgs, null);

        int productQty = C.NO_RESULT;
        if (cursor != null && cursor.getCount() != C.NULL) {
            cursor.moveToFirst();
            productQty = cursor.getInt(0);
            cursor.close();
        }
        return productQty;
    }

    /**
     * A method to update the database for a specific Products inventory count.
     * @param context need to pass in context as this is required to access the ContentResolver
     * @param productId the _ID of the product for update.
     * @return returns the number of rows updated.
     */
    public static int updateProductQty(Context context, long productId, int quantity){

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ProductsTable.QTY_ON_HAND, quantity);

        String selection = DataBaseContract.ProductsTable._ID + DataBaseContract.WHERE_EQUALS;
        String[] selectionArgs = {String.valueOf(productId)};
        Uri uri = DataBaseProvider.PRODUCTS_URI;
        return context.getContentResolver().update(uri, cv, selection, selectionArgs);
    }
}
