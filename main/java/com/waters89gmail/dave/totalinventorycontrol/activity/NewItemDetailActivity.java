package com.waters89gmail.dave.totalinventorycontrol.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.object_classes.Product;

/**
 * An activity to edit a product in the database or to add a new product to the database.
 */
public class NewItemDetailActivity extends AppCompatActivity {

    String requestType;
    long productId;
    FloatingActionButton fab;

    EditText productNameEditText;
    EditText productNumberEditText;
    EditText productDescriptionEditText;
    EditText productCategoryEditText;
    EditText productPriceEditText;
    EditText quantityOnHandEditText;
    EditText productTypeEditText;

    ImageView imageView1;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_detail_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productNameEditText = (EditText)findViewById(R.id.product_name_edit_text);
        productNumberEditText = (EditText)findViewById(R.id.product_number_edit_text);
        productDescriptionEditText = (EditText)findViewById(R.id.product_description_edit_text);
        productCategoryEditText = (EditText)findViewById(R.id.product_category_edit_text);
        productPriceEditText = (EditText)findViewById(R.id.price_edit_text);
        quantityOnHandEditText = (EditText)findViewById(R.id.quantity_on_hand_edit_text);
        productTypeEditText = (EditText)findViewById(R.id.type_edit_text);

        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if(getIntent()!=null) {
            Intent intent = getIntent();
            requestType = intent.getStringExtra(MainActivity.ACTIVITY_REQUEST);
            if (requestType.equals("edit")) {
                Product product = (Product) intent.getSerializableExtra(MainActivity.PRODUCT);
                getSupportActionBar().setTitle(getString(R.string.edit_product_details));
                editActivity(product);
            } else if (requestType.equals("add")) {
                getSupportActionBar().setTitle(getString(R.string.new_product_detail_activity));
                addActivity();
            }
        }
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void editActivity(final Product product){

        if(product != null){
            productId = product.getmProductId();
            productNameEditText.setText(product.getmName());
            productNumberEditText.setText(String.valueOf(product.getmNumber()));
            productDescriptionEditText.setText(product.getmDescription());
            productCategoryEditText.setText(product.getmCategory());
            productPriceEditText.setText(String.valueOf(product.getmPrice()));
            quantityOnHandEditText.setText(String.valueOf(product.getmQtyOnHand()));
            productTypeEditText.setText(product.getmType());
        }

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Product editedProduct = createProduct();

                    String selection = DataBaseContract.ProductsTable._ID + DataBaseContract.WHERE_EQUALS;
                    String[] selectionArgs = {String.valueOf(productId)};
                    Uri uri = DataBaseProvider.CONTENT_URI;
                    getContentResolver().update(uri,getContentValues(editedProduct),selection,selectionArgs);

                    Snackbar.make(view, "Successfully modified product data!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(MainActivity.PRODUCT,editedProduct);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }

    private void addActivity() {

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getContentResolver().insert(DataBaseProvider.CONTENT_URI, getContentValues(createProduct())) != null) {

                        Snackbar.make(view, "Successfully added product to DataBase!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }else{
                        Snackbar.make(view, "Unfortunately something went wrong and product wasn't added to DataBase!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                }
            });
        }
    }

    private Product createProduct(){

        return new Product(productNameEditText.getText().toString(),
                Long.valueOf(productNumberEditText.getText().toString()),
                productDescriptionEditText.getText().toString(),
                productCategoryEditText.getText().toString(),
                Double.valueOf(productPriceEditText.getText().toString()),
                Integer.valueOf(quantityOnHandEditText.getText().toString()),
                productTypeEditText.getText().toString(),
                "uri");
    }

    private ContentValues getContentValues(Product product){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }else if (id == R.id.action_settings) {
                return true;
        }
        return super.onOptionsItemSelected(item);
        }
}
