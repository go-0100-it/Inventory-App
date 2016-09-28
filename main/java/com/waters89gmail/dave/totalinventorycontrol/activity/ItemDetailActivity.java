package com.waters89gmail.dave.totalinventorycontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.object_classes.Product;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private static final String REQUEST_TYPE = "edit";
    private static final int EDIT_PRODUCT_REQUEST_CODE = 200;
    private long productId = 0;

    TextView productName;
    TextView productNumber;
    TextView productDescription;
    TextView productCategory;
    TextView productPrice;
    TextView quantityOnHand;
    TextView productType;

    ImageView imageView;
    AppBarLayout appBar;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Product  product;
        if (getIntent() != null) {
            Intent intent = getIntent();
            product = (Product) intent.getSerializableExtra(MainActivity.PRODUCT);
            productId = product.getmProductId();
        }else{
            product = null;
        }
        productName = (TextView)findViewById(R.id.product_name);
        productNumber = (TextView)findViewById(R.id.product_number);
        productDescription = (TextView)findViewById(R.id.product_description);
        productCategory = (TextView)findViewById(R.id.product_category);
        productPrice = (TextView)findViewById(R.id.price);
        quantityOnHand = (TextView)findViewById(R.id.quantity_on_hand);
        productType = (TextView)findViewById(R.id.type);
        imageView = (ImageView)findViewById(R.id.imgBackdrop);
        appBar = (AppBarLayout)findViewById(R.id.app_bar);
        collapsingToolBar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        if(product != null){
            setTextUi(product);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ItemDetailActivity.this, NewItemDetailActivity.class);
                    intent.putExtra(MainActivity.ACTIVITY_REQUEST, REQUEST_TYPE);
                    intent.putExtra(MainActivity.PRODUCT, product);
                    startActivityForResult(intent, EDIT_PRODUCT_REQUEST_CODE);
                }
            });
        }

        final FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ItemDetailActivity.this, NewItemDetailActivity.class);
                    intent.putExtra(MainActivity.ACTIVITY_REQUEST,REQUEST_TYPE);
                    intent.putExtra(MainActivity.PRODUCT, product);
                    startActivityForResult(intent,EDIT_PRODUCT_REQUEST_CODE);
                }
            });
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(verticalOffset > -750){
                    fab2.setVisibility(View.GONE);
                }else{
                    fab2.setVisibility(View.VISIBLE);
                }

                if(verticalOffset > -1215) {
                   collapsingToolBar.setTitle("");
                }else{
                    collapsingToolBar.setTitle(getString(R.string.product_detail_activity));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_detail, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDIT_PRODUCT_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Product edittedProduct = (Product) data.getSerializableExtra(MainActivity.PRODUCT);

                if(edittedProduct != null){
                    setTextUi(edittedProduct);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO
            }
        }
    }//onActivityResult

    private void setTextUi(Product product){

        productName.setText(product.getmName());
        productNumber.setText(String.valueOf(product.getmNumber()));
        productDescription.setText(product.getmDescription());
        productCategory.setText(product.getmCategory());
        productPrice.setText(String.valueOf(product.getmPrice()));
        quantityOnHand.setText(String.valueOf(product.getmQtyOnHand()));
        productType.setText(product.getmType());
    }
}
