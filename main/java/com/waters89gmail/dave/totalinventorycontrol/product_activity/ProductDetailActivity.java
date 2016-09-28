package com.waters89gmail.dave.totalinventorycontrol.product_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.EditInventoryDialog;
import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.AddAgentsActivity;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.BusinessAgent;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.DialogUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.ImageUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.IntentUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;


/**
 * An activity to display the selected Products details and image. The activity enables the user to edit the details, modify the inventory amount and
 * submit an order request. The order request can only be made if the product has a Business Agent associated as a supplier.  If the product displayed does not have a Business Agent
 * associated supplier, the user can create an association from this activity.
 */
public class ProductDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,MultiUseDialog.MultiUseDialogListener,EditInventoryDialog.EditInventoryDialogListener{



    private Product product;
    private String agentPhoneNum;

    TextView productName, productNumber, productDescription, productCategory, productPrice, quantityOnHand, productType;

    ImageView imageView;
    ProgressBar progressBar;
    RelativeLayout imageLayout2;
    AppBarLayout appBar;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolBar;
    BitmapWorkerTask bitmapWorkerTask;

    //saving the Product Object to re-use if the activity needs to be recreated.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(C.PRODUCT_SAVED_INSTANCE, product);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //checking to see if this activity has been created already, if has not been created extract Products Object from extras.
        //If it has been created extract Products Object from the bundle saved @onSaveInstantState.
        if (savedInstanceState == null) {
            //checking to see if intent exists, if it exists extract the extras
            if (getIntent() != null) {
                Intent intent = getIntent();
                product = (Product) intent.getSerializableExtra(C.PRODUCT);
            }
        }else{
            product = (Product) savedInstanceState.getSerializable(C.PRODUCT_SAVED_INSTANCE);
        }
        //setting view variables
        productName = (TextView) findViewById(R.id.product_name);
        productNumber = (TextView) findViewById(R.id.product_number);
        productDescription = (TextView) findViewById(R.id.product_description);
        productCategory = (TextView) findViewById(R.id.product_category);
        productPrice = (TextView) findViewById(R.id.price);
        quantityOnHand = (TextView) findViewById(R.id.quantity_on_hand);
        productType = (TextView) findViewById(R.id.type);
        imageView = (ImageView) findViewById(R.id.imgBackdrop);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        progressBar = (ProgressBar) findViewById(R.id.image_progress);
        imageLayout2 = (RelativeLayout) findViewById(R.id.imageLayout);
        collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //setting up view if product is not null.
        if (product != null) {
            setTextUi(product);
        }
        //setting onClickListener for the editFab button.
        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        if (editFab != null) {
            editFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Calling EditProductActivity to allow user to edit the Product details.
                    //Passing a Product Object as an extra with intent, and expecting a modified Product Object as returned data, if result not cancelled
                    Intent intent = new Intent(ProductDetailActivity.this, EditProductActivity.class);
                    intent.putExtra(C.PRODUCT, product);
                    startActivityForResult(intent, C.EDIT_PRODUCT_REQUEST_CODE);
                }
            });
        }
        //setting onClickListener for the orderMoreFab button.
        final FloatingActionButton orderMoreFab = (FloatingActionButton) findViewById(R.id.order_more_fab);
        if (orderMoreFab != null) {
            orderMoreFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchOrderRequest(ProductDetailActivity.this);
                }
            });
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //showing the orderFab button when the collapsingToolbar is 1/3 collapsed
        //showing the toolBar title only when fully collapsed.
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(verticalOffset > collapsingToolBar.getMeasuredHeight()/3*-1){
                    orderMoreFab.setVisibility(View.GONE);
                }else{
                    orderMoreFab.setVisibility(View.VISIBLE);
                }

                if(verticalOffset > collapsingToolBar.getMeasuredHeight()/-1.5) {
                   collapsingToolBar.setTitle(C.EMPTY_STRING);
                }else{
                    collapsingToolBar.setTitle(getString(R.string.product_detail_activity));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                navigateUpTo(new Intent(this, ProductListFragment.class));
                return true;
            case R.id.action_modify_inventory:
                DialogUtils.showEditInventoryDialog(this,C.EDIT_INVENTORY_DIALOG_TAG,product);
                return true;
            case R.id.action_buy_product:
                dispatchOrderRequest(ProductDetailActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Getting results returned from intents started with startActivityForResults
     * @param requestCode returns the request code set when starting intent, so we know which intent is returning the results
     * @param resultCode returns either RESULT_OK or RESULT_CANCELLED, RESULT_OK is returned if the desired actions have occurred or if the desired data is being return.
     * @param data returns the intent data containing the extras passed back from the activity started for results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //getting results from activities started for results.
        if (requestCode == C.EDIT_PRODUCT_REQUEST_CODE) {
            // this code runs if this result is from the EditProductActivity
            if(resultCode == Activity.RESULT_OK){
                //setting the Product Object variable "product" to the modified Product Object returned as an extra from the editProductActivity
                product = (Product) data.getSerializableExtra(C.PRODUCT);
                //updating the UI with the new data returned
                setTextUi(product);
                // notifying the user the edit was saved successfully. We know this is true because RESULT_OK is only set if the Product was
                // successfully updated in the editProductActivity.
                Snackbar.make(toolbar, getString(R.string.saved_changes), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(toolbar, getString(R.string.changes_not_saved), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
        }else if(requestCode == C.ASSOCIATE_PRODUCT_REQUEST){
            // this code runs if this result is from the AssociateAgentDialogFragment
            if(resultCode == Activity.RESULT_OK){
                Snackbar.make(toolbar, "New Agent and association created successfully!", Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(toolbar, "Creation of Agent and Association was cancelled.", Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
        }
    }//onActivityResult

    /**
     * This method sets the text of the UI TextViews with the extracted values from the Product Object.
     * @param product The Product Object to extract values from, which, will be used to populate the UI TextViews.
     */
    private void setTextUi(Product product){

        productName.setText(product.getmName());
        productNumber.setText(String.valueOf(product.getmNumber()));
        productDescription.setText(product.getmDescription());
        productCategory.setText(product.getmCategory());
        productPrice.setText(String.valueOf(product.getmPrice()));
        quantityOnHand.setText(String.valueOf(product.getmQtyOnHand()));
        productType.setText(product.getmType());
        imageView.setImageResource(R.drawable.photo_blank);
        progressBar.setVisibility(View.VISIBLE);
        bitmapWorkerTask = new BitmapWorkerTask();
        if (Permissions.confirmStoragePermissions(this)) {
            //Permission is GRANTED so, load image.
            bitmapWorkerTask.execute(product);}
    }

    /**
     * An AsyncTask to load the Product image in a separate thread.
     */
    class BitmapWorkerTask extends AsyncTask<Product, Void, Bitmap> {
        private Product product = null;

        @Override
        protected Bitmap doInBackground(Product... params) {
            product = params[0];
            return ImageUtils.rotateImage(product.getmImage());
        }

        // Once complete, see if the ImageView still exists and if it does, set the bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     *  This method checks to see if an Agent has been associated as a supplier for this product.  see {@link DataBaseUtils#checkForAgent(Context, long)}.
     *  <p>
     *  If the association <strong>does not exist</strong> the method will return C.NO_RESULT and call a Dialog method to get further instructions from user.  see {@link DialogUtils#showMultiUseDialog(AppCompatActivity, String, int, int, int, int)}.
     *  If the association <strong>does exist</strong> the agentId will be returned and set to the variable agentKey.
     *  </p>
     *  Then we check what the preferred method of contact is that has been specified for this agent @agentId.
     *  If the preferred method is <strong>Phone</strong>, we pass in the agents phone number to the method that will start the phone intent. see {@link IntentUtils#phoneAgent(Context, String)}.
     *  If the preferred method is <strong>Email</strong>, we pass in the agents email address and appropriate strings for the order email to the method that will start the email intent.  see {@link IntentUtils#sendEmail(Context, String, String, String, String)}.
     * @param context we pass in the context here so we can pass it to the downstream methods for starting intents.
     * </p>
     */
    private void dispatchOrderRequest(Context context){

        long agentKey = DataBaseUtils.checkForAgent(this,product.getmProductId());
        if (agentKey != C.NO_RESULT) {
            String agentEmail;
            BusinessAgent agent = DataBaseUtils.getAgent(this,agentKey);
            if (agent != null) {
                agentPhoneNum = agent.getmContact1();
                agentEmail = agent.getmEmail();
                if(agent.getmPreferredContact().equals(getString(R.string.phone))){

                    if (Permissions.confirmPhonePermissions(this)){
                        IntentUtils.phoneAgent(context,agentPhoneNum);}
                }else if(agent.getmPreferredContact().equals(getString(R.string.email))){
                    IntentUtils.sendEmail(this,agentEmail,getString(R.string.order_email_subject),getString(R.string.order_email_body_title)+"\n\n"+
                            getString(R.string.order_email_body_ln1)+"\n\n"+
                            getString(R.string.order_email_body_ln2) + C.SPACE + product.getmName()+ "\n"+
                            getString(R.string.order_email_body_ln3) + C.SPACE + product.getmNumber()+"\n"+
                            getString(R.string.order_email_body_ln4)+"\n",getString(R.string.order_email_send_with));
                }
            }
        }else{
            DialogUtils.showMultiUseDialog(this,C.ADD_SUPPLIER_DIALOG_TAG,R.string.add_supplier_dialog_message,R.string.add_supplier_dialog_positive,
                    R.string.add_supplier_dialog_neutral,R.string.add_supplier_dialog_negative);
        }
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Intent intent;
        switch (dialog.getTag()){
            case C.ADD_SUPPLIER_DIALOG_TAG:
                intent = new Intent(ProductDetailActivity.this, AddAgentsActivity.class);
                intent.putExtra(C.PRODUCT_FOR_ASSOCIATION,product);
                startActivityForResult(intent,C.ASSOCIATE_PRODUCT_REQUEST);
                break;
            case C.EDIT_INVENTORY_DIALOG_TAG:
                int qty =  DataBaseUtils.getProductQty(this,product.getmProductId());
                if (qty != -1){
                    product.setmQtyOnHand(qty);
                    quantityOnHand.setText(String.valueOf(qty));
                    dialog.dismiss();
                    Snackbar.make(toolbar, "Updated inventory Amount.", Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
        }
    }

    /**
     * Overriding custom onClickListener interface to handle dialog neutral button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            case C.ADD_SUPPLIER_DIALOG_TAG:
                DialogUtils.showAssociateAgentDialog(this,C.ASSOCIATE_AGENT_DIALOG,product);
                break;
        }
    }

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            case C.PHONE_PERMISSIONS_DIALOG:
                Permissions.requestPhonePermission(this);
                break;
            case C.STORAGE_PERMISSIONS_DIALOG:
                Permissions.requestStoragePermissions(this);
                break;
            default:
                break;
        }
        dialog.dismiss();
    }

    /**
     * Receiving call backs here from requestPermissions
     * @param requestCode the request code set when calling permissions request.
     * @param permissions the string array of permissions requested.
     * @param grantResults an int array of the granted results corresponding to the permissions string array
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            //if phone permissions were requested and permission was granted, call the method to phone the Agent
            case C.REQUEST_CALL_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    IntentUtils.phoneAgent(ProductDetailActivity.this,agentPhoneNum);
                }
                break;
            //if storage permissions were requested and permission was granted, call the method to load the product image.
            case C.REQUEST_EXTERNAL_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    bitmapWorkerTask.execute(product);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


}
