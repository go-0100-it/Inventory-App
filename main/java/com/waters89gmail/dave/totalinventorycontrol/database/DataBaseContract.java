package com.waters89gmail.dave.totalinventorycontrol.database;

import android.provider.BaseColumns;

/**
 * Created by WatersD on 8/9/2016.
 */
public class DataBaseContract {

    // Logcat tag
    public static final String LOG_TAG = ("MY_LOG");

    public DataBaseContract() {}

    /* Inner class that defines the ProductsTable contents */
    public static abstract class ProductsTable implements BaseColumns {

        //Products table - name
        public static final String TABLE_PRODUCTS = "products_table";

        //Products table - column names
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String NUMBER = "number";
        public static final String DESCRIPTION = "description";
        public static final String CATEGORY = "category";
        public static final String PRICE = "price";
        public static final String QTY_ON_HAND = "qty_on_hand";
        public static final String TYPE = "type";
        public static final String IMAGE_SOURCE = "image";

    }

    /* Inner class that defines the Transactions Table contents */
    public static abstract class TransactionTable implements BaseColumns {

        //Transactions table - name
        public static final String TABLE_TRANSACTIONS = "transactions_table";

        //Transactions table - column names
        public static final String _ID = "_id";
        public static final String ORDER_QTY = "order_qty";
        public static final String DATE = "date";
        public static final String AMOUNT = "amount";
        public static final String TRANS_TYPE = "trans_type";
        public static final String PAYMENT_TYPE = "payment_type";
        public static final String PAYMENT_STATUS = "payment_status";
        public static final String DELIVERY_RECEIPT_STATUS = "delivery_receipt_status";
        public static final String TRANS_METHOD = "transaction_method";

    }

    /* Inner class that defines the Business Agents Table contents */
    public static abstract class BusinessAgentsTable implements BaseColumns {

        //Business Agents table - name
        public static final String TABLE_BUSINESS_AGENTS = "business_agents_table";

        //Business Agents table - column names
        public static final String _ID = "_id";
        public static final String AGENT_NAME = "agent_name";
        public static final String ADDRESS_1 = "address_1";
        public static final String ADDRESS_2 = "address_2";
        public static final String EMAIL = "email";
        public static final String CONTACT_1 = "contact_1";
        public static final String CONTACT_2 = "contact_2";
        public static final String PREFERRED_CONTACT = "preferred_contact";
        public static final String WEBSITE = "website";
        public static final String COMPANY_NAME = "company_name";

    }

    /* Inner class that defines the Ordering Details Table contents */
    public static abstract class OrderingDetailsTable implements BaseColumns {

        //Ordering Details table - name
        public static final String TABLE_ORDERING_DETAILS = "ordering_details_table";

        //Ordering Details table - column names
        public static final String _ID = "_id";
        public static final String MIN_ORDER = "min_order";
        public static final String MAX_ORDER = "max_order";
        public static final String LEAD_TIME = "lead_time";
        public static final String COST = "cost";
        public static final String MIN_ON_HAND = "min_on_hand";
        public static final String PAYMENT_TYPE_1 = "payment_type_1";
        public static final String PAYMENT_TYPE_2 = "payment_type_2";
        public static final String ORDER_METHOD = "order_method";

    }

    /* Inner class that defines the Sales Key Table contents */
    public static abstract class SalesKeyTable implements BaseColumns {

        //Sales Key Table - name
        public static final String TABLE_SALES_KEY = "sales_key_table";

        //Sales Keys Table - column names
        public static final String _ID = "_id";
        public static final String AGENT_KEY = "agent_key";
        public static final String TRANS_KEY = "trans_key";
        public static final String PROD_KEY = "prod_key";
    }

    /* Inner class that defines the Purchases Key Table contents */
    public static abstract class PurchasesKeyTable implements BaseColumns {

        //Purchases Key Table - name
        public static final String TABLE_PURCHASES_KEY = "purchases_key_table";

        //Purchases Key Table - column names
        public static final String _ID = "_id";
        public static final String AGENT_KEY = "agent_key";
        public static final String TRANS_KEY = "trans_key";
        public static final String PROD_KEY = "prod_key";
    }

    /* Inner class that defines the Product-OrderingDetails Key Table contents */
    public static abstract class ProductOrderingDetailsKeyTable implements BaseColumns {

        //Product OrderingDetails Key Table - name
        public static final String TABLE_PRODUCT_ORDERING_DETAILS_KEY = "product_ordering_details_keys_table";

        //Product OrderingDetails Key Table - column names
        public static final String _ID = "_id";
        public static final String ORDER_DETAILS_KEY = "order_details_key";
        public static final String PROD_KEY = "prod_key";
    }

    /* Inner class that defines the Product-Agent Key Table contents */
    public static abstract class ProductAgentKeysTable implements BaseColumns {

        //Product-Agent Key Table - name
        public static final String TABLE_PRODUCT_AGENT_KEY = "product_agent_key_table";

        //Product-Agent Key Table - column names
        public static final String _ID = "_id";
        public static final String AGENT_KEY = "agent_key";
        public static final String PROD_KEY = "prod_key";
    }

    /* Inner class that defines the Product-Category Key Table contents */
    public static abstract class ProductCategoryKeysTable implements BaseColumns {

        //Product-Category Key Table - name
        public static final String TABLE_PRODUCT_CATEGORY_KEY = "product_category_key_table";

        //Product-Category Key Table - column names
        public static final String _ID = "_id";
        public static final String CATEGORY_KEY = "category_key";
        public static final String PROD_KEY = "prod_key";
    }

    // Database Version & Name
    // If the database schema is changed, the database version must be incremented.
    public static final String DATABASE_NAME = "database_name";
    public static final int DATABASE_VERSION = 1;

    //Constant values for
    public static final String TEXT_TYPE = "TEXT";
    private static final String OPEN_PAR = "(";
    private static final String SPACE = " ";
    private static final String CLOSE_PAR = ")";
    private static final String INT_TYPE = "INTEGER";
    private static final String NUMERIC_TYPE = "NUMERIC";
    private static final String COMMA = ",";
    private static final String INT_PK_AUTO = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String CREATE_TABLE = "CREATE TABLE";
    public static final String WHERE_EQUALS = "=?";
    public static final String WHERE_ID_EQUALS = "_id=";

    /*
    String for Creating the Products Table
     */
    public static final String SQL_CREATE_PRODUCTS_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.ProductsTable.TABLE_PRODUCTS + SPACE + OPEN_PAR +
                    ProductsTable._ID + SPACE + INT_PK_AUTO + COMMA +
        ProductsTable.NUMBER + SPACE + INT_TYPE + COMMA +
                    ProductsTable.NAME + SPACE + TEXT_TYPE + COMMA +
                    ProductsTable.DESCRIPTION + SPACE + TEXT_TYPE + COMMA +
                    ProductsTable.CATEGORY + SPACE + TEXT_TYPE + COMMA +
                    ProductsTable.PRICE + SPACE + NUMERIC_TYPE + COMMA +
                    ProductsTable.QTY_ON_HAND + SPACE + INT_TYPE + COMMA +
                    ProductsTable.TYPE + SPACE + TEXT_TYPE + COMMA +
                    ProductsTable.IMAGE_SOURCE + SPACE + TEXT_TYPE + CLOSE_PAR;
    /*
    String for Creating the BusinessAgentsTable
    */
    public static final String SQL_CREATE_BUSINESS_AGENTS_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.BusinessAgentsTable.TABLE_BUSINESS_AGENTS + SPACE + OPEN_PAR +
                    BusinessAgentsTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    BusinessAgentsTable.AGENT_NAME + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.ADDRESS_1 + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.ADDRESS_2 + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.EMAIL + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.CONTACT_1 + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.CONTACT_2 + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.PREFERRED_CONTACT + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.WEBSITE + SPACE + TEXT_TYPE + COMMA +
                    BusinessAgentsTable.COMPANY_NAME + SPACE + TEXT_TYPE + CLOSE_PAR;
    /*
    String for Creating the OrderingDetailsTable
    */
    public static final String SQL_CREATE_ORDERING_DETAILS_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.OrderingDetailsTable.TABLE_ORDERING_DETAILS + SPACE + OPEN_PAR +
                    OrderingDetailsTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    OrderingDetailsTable.MIN_ORDER + SPACE + INT_TYPE + COMMA +
                    OrderingDetailsTable.MAX_ORDER + SPACE + INT_TYPE + COMMA +
                    OrderingDetailsTable.LEAD_TIME + SPACE + INT_TYPE + COMMA +
                    OrderingDetailsTable.COST + SPACE + NUMERIC_TYPE + COMMA +
                    OrderingDetailsTable.MIN_ON_HAND + SPACE + INT_TYPE + COMMA +
                    OrderingDetailsTable.PAYMENT_TYPE_1 + SPACE + TEXT_TYPE + COMMA +
                    OrderingDetailsTable.PAYMENT_TYPE_2 + SPACE + TEXT_TYPE + COMMA +
                    OrderingDetailsTable.ORDER_METHOD + SPACE + TEXT_TYPE + CLOSE_PAR;
    /*
    String for Creating the TransactionTable
    */
    public static final String SQL_CREATE_TRANSACTION_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.TransactionTable.TABLE_TRANSACTIONS + SPACE + OPEN_PAR +
                    TransactionTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    TransactionTable.ORDER_QTY + SPACE + INT_TYPE + COMMA +
                    TransactionTable.DATE + SPACE + TEXT_TYPE + COMMA +
                    TransactionTable.AMOUNT + SPACE + NUMERIC_TYPE + COMMA +
                    TransactionTable.TRANS_TYPE + SPACE + TEXT_TYPE + COMMA +
                    TransactionTable.PAYMENT_TYPE + SPACE + TEXT_TYPE + COMMA +
                    TransactionTable.PAYMENT_STATUS + SPACE + TEXT_TYPE + COMMA +
                    TransactionTable.DELIVERY_RECEIPT_STATUS + SPACE + TEXT_TYPE + COMMA +
                    TransactionTable.TRANS_METHOD + SPACE + TEXT_TYPE + CLOSE_PAR;
    /*
    String for Creating the SalesKeyTable
    */
    public static final String SQL_CREATE_SALES_KEY_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.SalesKeyTable.TABLE_SALES_KEY + SPACE + OPEN_PAR +
                    SalesKeyTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    SalesKeyTable.TRANS_KEY + SPACE + INT_TYPE + COMMA +
                    SalesKeyTable.AGENT_KEY + SPACE + INT_TYPE + COMMA +
                    SalesKeyTable.PROD_KEY + SPACE + INT_TYPE + CLOSE_PAR;
    /*
    String for Creating the ProductOrderingDetailsKeyTable
    */
    public static final String SQL_CREATE_PRODUCT_ORDERING_DETAILS_KEY_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.ProductOrderingDetailsKeyTable.TABLE_PRODUCT_ORDERING_DETAILS_KEY + SPACE + OPEN_PAR +
                    ProductOrderingDetailsKeyTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    ProductOrderingDetailsKeyTable.ORDER_DETAILS_KEY + SPACE + INT_TYPE + COMMA +
                    ProductOrderingDetailsKeyTable.PROD_KEY + SPACE + INT_TYPE + CLOSE_PAR;
    /*
    String for Creating the PurchasesKeyTable
    */
    public static final String SQL_CREATE_PURCHASES_KEY_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.PurchasesKeyTable.TABLE_PURCHASES_KEY + SPACE + OPEN_PAR +
                    PurchasesKeyTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    PurchasesKeyTable.TRANS_KEY + SPACE + INT_TYPE + COMMA +
                    PurchasesKeyTable.AGENT_KEY + SPACE + INT_TYPE + COMMA +
                    PurchasesKeyTable.PROD_KEY + SPACE + INT_TYPE + CLOSE_PAR;
    /*
    String for Creating the ProductAgentKeysTable
    */
    public static final String SQL_CREATE_PRODUCT_AGENT_KEY_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.ProductAgentKeysTable.TABLE_PRODUCT_AGENT_KEY + SPACE + OPEN_PAR +
                    ProductAgentKeysTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    ProductAgentKeysTable.PROD_KEY + SPACE + INT_TYPE + COMMA +
                    ProductAgentKeysTable.AGENT_KEY + SPACE + INT_TYPE + CLOSE_PAR;
    /*
    String for Creating the ProductCategoryKeysTable
    */
    public static final String SQL_CREATE_PRODUCT_CATEGORY_KEY_TABLE =
            CREATE_TABLE + SPACE + DataBaseContract.ProductCategoryKeysTable.TABLE_PRODUCT_CATEGORY_KEY + SPACE + OPEN_PAR +
                    ProductCategoryKeysTable._ID + SPACE + INT_PK_AUTO + COMMA +
                    ProductCategoryKeysTable.PROD_KEY + SPACE + INT_TYPE + COMMA +
                    ProductCategoryKeysTable.CATEGORY_KEY + SPACE + INT_TYPE + CLOSE_PAR;
}
