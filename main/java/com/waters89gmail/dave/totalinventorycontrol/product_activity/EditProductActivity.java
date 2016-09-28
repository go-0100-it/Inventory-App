package com.waters89gmail.dave.totalinventorycontrol.product_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.MainActivity;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.AddAgentsActivity;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.AgentDetailActivity;
import com.waters89gmail.dave.totalinventorycontrol.agent_activity.BusinessAgent;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.DialogUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.ImageUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * An activity to edit a Product in the database or to add a new Product to the database.
 */
public class EditProductActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback,MultiUseDialog.MultiUseDialogListener {

    private long productId;
    private String origImagePath = C.EMPTY_STRING;
    private String cameraImagePath = C.EMPTY_STRING;
    private String newImagePath = C.EMPTY_STRING;

    private Product product = null;
    private long agentKey;

    private FloatingActionButton saveFab;

    private EditText productNameEditText, productNumberEditText, productDescriptionEditText, productPriceEditText, quantityOnHandEditText, productImageFilePath;
    private Spinner productCategoryEditText, productTypeEditText;

    //saving the agentKey to the outState bundle to persist this data through the activity lifecycle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(C.AGENT_ID, agentKey);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_product_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // checking if the Bundle savedInstantState has data, if it does re-set the agentKey variable.
        if (savedInstanceState != null) {
            this.agentKey = savedInstanceState.getLong(C.AGENT_ID);
        }
        //finding views and setting to variables for further reference
        productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        productNumberEditText = (EditText) findViewById(R.id.product_number_edit_text);
        productDescriptionEditText = (EditText) findViewById(R.id.product_description_edit_text);
        productCategoryEditText = (Spinner) findViewById(R.id.product_category_edit_text);
        productPriceEditText = (EditText) findViewById(R.id.price_edit_text);
        quantityOnHandEditText = (EditText) findViewById(R.id.quantity_on_hand_edit_text);
        productTypeEditText = (Spinner) findViewById(R.id.type_edit_text);
        productImageFilePath = (EditText) findViewById(R.id.product_image_path);

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageBtn);
        imageButton.setImageResource(R.drawable.edit_pic_small_white);

        saveFab = (FloatingActionButton) findViewById(R.id.fab);

        //Checking if Intent exists. If it exists get Product Object passed as extra.
        if (getIntent() != null) {
            Intent intent = getIntent();
            product = (Product) intent.getSerializableExtra(C.PRODUCT);

            // Calling method to add Product details to Views.
            populateUi(product);

            if (saveFab != null) {
                saveFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editActivity(view);
                    }
                });
            }
        }
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.edit_product_details));
        }

        assert imageButton != null;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checking if permissions are granted, if they are call method to show Image dialog for user to select image source.
            if (Permissions.confirmStoragePermissions(EditProductActivity.this)){
                DialogUtils.showMultiUseDialog(EditProductActivity.this,C.IMAGE_DIALOG_TAG,R.string.image_dialog_message,R.string.image_from_file,0,R.string.image_from_camera);
            }
            }
        });
    }

    /**
     * A method to extract data from the Product Object and populate the EditText Views and Spinners
     * @param product the Product Object to extract data from to populate the views.
     */
        private void populateUi(Product product){
        //creating usable string arrays from resource string-arrays for Spinner lists.
        String[] productCategoryArray = getResources().getStringArray(R.array.product_category_array);
        String[] productTypeArray = getResources().getStringArray(R.array.product_type_array);
        if (product != null) {
            productId = product.getmProductId();
            productNameEditText.setText(product.getmName());
            productNumberEditText.setText(String.valueOf(product.getmNumber()));
            productDescriptionEditText.setText(product.getmDescription());
            int i = Arrays.asList(productCategoryArray).indexOf(product.getmCategory());
            productCategoryEditText.setSelection(i);
            productPriceEditText.setText(String.valueOf(product.getmPrice()));
            quantityOnHandEditText.setText(String.valueOf(product.getmQtyOnHand()));
            i = Arrays.asList(productTypeArray).indexOf(product.getmType());
            productTypeEditText.setSelection(i);
            productImageFilePath.setText(product.getmImage());
            origImagePath = product.getmImage();
        }
    }

    /**
     * A method to verify user inputted data and update DataBase with the modified Product
     */
    private void editActivity(View view) {
        // Verifying that the user inputted data into all fields and that the image path is present.
        if (verifyDataEntry(view)) {
            //deleting the original image file if, a new one exists and if the new one is not the same as the original.
            if ((newImagePath != null && !newImagePath.equals(C.EMPTY_STRING)) && !origImagePath.equals(newImagePath)) {
                ImageUtils.deleteImage(origImagePath);
            }
            Product editedProduct = createProduct();

            // Defining the ContentResolver Args, required for the update method.
            String selection = DataBaseContract.ProductsTable._ID + DataBaseContract.WHERE_EQUALS;
            String[] selectionArgs = {String.valueOf(productId)};
            Uri uri = DataBaseProvider.PRODUCTS_URI;

            int result = getContentResolver().update(uri, DataBaseUtils.createProductContentValues(editedProduct), selection, selectionArgs);

            //creating an intent to return the result to the calling activity
            Intent returnIntent = new Intent();
            //returning the edited product and a RESULT_OK if the result returned from the ContentResolver is greater than C.NULL(0) else returning result cancelled.
            if(result > C.NULL) {
                returnIntent.putExtra(C.PRODUCT, editedProduct);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }else {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }

    /**
     * Method used to validate user input, to ensure required data for further app functionality is entered at the time of Object creation and at the time of editing.
     * @param view requires a view object to set the snackBar.
     * @return returns a boolean value of true if all the required conditions are met.
     * Returns false if all required conditions are not met, after displaying a snackBar prompting user for required action by requesting focus on missing view.
     */
    private boolean verifyDataEntry(View view){
        View viewToFocus;
        int messageRes;
        // Check if user has inputted text for the Product Name.
        if (!productNameEditText.getText().toString().equals(C.EMPTY_STRING)){
            // Check if user has inputted text for the Product Number.
            if(!productNumberEditText.getText().toString().equals(C.EMPTY_STRING)){
                // Check if user has inputted text for the Product Description.
                if(!productDescriptionEditText.getText().toString().equals(C.EMPTY_STRING)){
                    // Check if user has selected a list item for the Product Category.
                    if(!productCategoryEditText.getSelectedItem().toString().equals(getString(R.string.please_select))){
                        // Check if user has inputted text for the Product Price.
                        if(!productPriceEditText.getText().toString().equals(C.EMPTY_STRING)){
                            // Check if user has selected a list item for the Product Type.
                            if(!productTypeEditText.getSelectedItem().toString().equals(getString(R.string.please_select))){
                                // Check if the file path for an image exists.
                                if(!productImageFilePath.getText().toString().equals(C.EMPTY_STRING)){
                                    //if all the above conditions are met, return true.
                                    return true;
                                }else{ // If any of the above fields are empty or still default selections set view for focus.
                                    viewToFocus = productImageFilePath;
                                    messageRes = R.string.add_product_missing_data;
                                }
                            }else{
                                viewToFocus = productTypeEditText;
                                messageRes = R.string.add_product_missing_data;
                            }
                        }else{
                            viewToFocus = productPriceEditText;
                            messageRes = R.string.add_product_missing_data;
                        }
                    }else{
                        viewToFocus = productCategoryEditText;
                        messageRes = R.string.add_product_missing_data;
                    }
                }else{
                    viewToFocus = productDescriptionEditText;
                    messageRes = R.string.add_product_missing_data;
                }
            }else{
                viewToFocus = productNumberEditText;
                messageRes = R.string.add_product_missing_data;
            }
        }else{
            viewToFocus = productNameEditText;
            messageRes = R.string.add_product_missing_data;
        }
        snackBarToRequestFocus(view,messageRes,R.string.ok,viewToFocus);
        return false;
    }

    /**
     * Common method to show SnackBar and set focus on a view.
     * @param view requires a view object to set the snackBar.
     * @param messageStringRes int value of a string resource. Corresponding string is used as the message displayed in the snackBar.
     * @param actionBtnStringRes int value of a string resource. Corresponding string used as the text for the displayed action button.
     * @param fieldToFocus The action taken sets focus on a view, requires the view to accept focus.
     */
    private void snackBarToRequestFocus(View view, @StringRes int messageStringRes, @StringRes int actionBtnStringRes, final View fieldToFocus ){
        Snackbar.make(view, messageStringRes,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionBtnStringRes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fieldToFocus == (productCategoryEditText)){
                            productCategoryEditText.performClick();
                        }else if(fieldToFocus == productTypeEditText){
                            productTypeEditText.performClick();
                        }else{
                            fieldToFocus.requestFocus();
                        }
                    }
                })
                .show();
    }

    /**
     * @return returns a new product created from the data extracted from the Views
     */
    private Product createProduct() {
        return new Product(productId,productNameEditText.getText().toString(),
                Long.valueOf(productNumberEditText.getText().toString()),
                productDescriptionEditText.getText().toString(),
                productCategoryEditText.getSelectedItem().toString(),
                Double.valueOf(productPriceEditText.getText().toString()),
                Integer.valueOf(quantityOnHandEditText.getText().toString()),
                productTypeEditText.getSelectedItem().toString(),
                productImageFilePath.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                // Calling the deleteImage method to delete the unsaved image if one exists.
                if (!origImagePath.equals(newImagePath)) {
                    ImageUtils.deleteImage(newImagePath);
                }
                // Sending extra with intent so MainActivity knows to add the ProductFragment
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra(C.FROM_ACTIVITY,C.EDIT_PRODUCT);
                navigateUpTo(intent);
                return true;
            case R.id.action_add_edit_association:
                // Checking if a BusinessAgent has been associated to this product to choose which dialog to show.
                agentKey = DataBaseUtils.checkForAgent(this,product.getmProductId());
                if(agentKey != C.NO_RESULT){
                    // If an association exist
                    DialogUtils.showMultiUseDialog(EditProductActivity.this,C.EDIT_SUPPLIER_DIALOG_TAG,R.string.edit_supplier_dialog_message,R.string.edit_supplier_dialog_positive,
                            R.string.edit_supplier_dialog_neutral,R.string.edit_supplier_dialog_negative);
                }else{
                    // If an association does not exist
                    DialogUtils.showMultiUseDialog(EditProductActivity.this,C.ADD_SUPPLIER_DIALOG_TAG,R.string.add_supplier_dialog_message,R.string.add_supplier_dialog_positive,
                            R.string.add_supplier_dialog_neutral,R.string.add_supplier_dialog_negative);
                }
                return true;
            case R.id.action_delete:
                // Showing a dialog to get delete confirmation.
                DialogUtils.showMultiUseDialog(this,C.DELETE_PRODUCT_DIALOG_TAG,R.string.delete_product_dialog_massage,R.string.delete_product_dialog_positive,C.NULL,R.string.delete_product_dialog_negative);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Intent intent;
        switch (dialog.getTag()){
            case C.IMAGE_DIALOG_TAG:
                dispatchChoosePictureIntent();
                break;
            case C.ADD_SUPPLIER_DIALOG_TAG:
                intent = new Intent(EditProductActivity.this, AddAgentsActivity.class);
                intent.putExtra(C.PRODUCT_FOR_ASSOCIATION,product);
                startActivityForResult(intent,C.ASSOCIATE_PRODUCT_REQUEST);
                break;
            case C.EDIT_SUPPLIER_DIALOG_TAG:
                intent = new Intent(EditProductActivity.this, AgentDetailActivity.class);
                BusinessAgent agent = DataBaseUtils.getAgent(this,agentKey);
                agent.setmAgentId(agentKey);
                intent.putExtra(C.AGENT_TO_VIEW,agent);
                startActivity(intent);
                break;
            case C.DELETE_PRODUCT_DIALOG_TAG:
                deleteProduct();
                break;
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
            case C.EDIT_SUPPLIER_DIALOG_TAG:
                if(DataBaseUtils.deleteProductAgentAssociation(this,productId) > C.NULL){
                    Snackbar.make(productCategoryEditText, getString(R.string.association_was_removed), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                break;
            default:
                dialog.dismiss();
                break;
        }
    }

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            case C.IMAGE_DIALOG_TAG:
                if (Permissions.confirmCameraPermissions(this)){
                    dispatchTakePictureIntent();}
                break;
            case C.ADD_SUPPLIER_DIALOG_TAG:
                dialog.dismiss();
                break;
            case C.EDIT_SUPPLIER_DIALOG_TAG:
                dialog.dismiss();
                break;
            case C.CAMERA_PERMISSIONS_DIALOG:
                Permissions.requestCameraPermissions(this);
                break;
            case C.STORAGE_PERMISSIONS_DIALOG:
                Permissions.requestStoragePermissions(this);
                break;
        }
    }

    /**
     * A method to delete a product from the DataBase
     */
    private void deleteProduct(){
        int[] result = DataBaseUtils.deleteProduct(this,productId);

        if(result[0] > C.NULL){
            ImageUtils.deleteImage(product.getmImage());
            Intent intent = new Intent(EditProductActivity.this,MainActivity.class);
            intent.putExtra(C.DELETED_TYPE,C.PRODUCT);
            intent.putExtra(C.COUNT,result[1]);
            navigateUpTo(intent);

        }else{
            Snackbar.make(saveFab.getRootView(), getString(R.string.deleted_product_failed), Snackbar.LENGTH_LONG)
                    .setAction(C.ACTION, null).show();
        }
    }

    /**
     * A method to start a photoPickerIntent
     */
    private void dispatchChoosePictureIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(C.IMAGE_TYPE);
        startActivityForResult(photoPickerIntent, C.GALLERY_REQUEST);
    }

    /**
     * A method that creates a new file to store image and starts an Activity to launch camera for image capture
     * @return returns new image file
     */
    private File dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri tempUri;
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File imageFile = null;

            try {
                imageFile = ImageUtils.createImageFile();
                cameraImagePath = imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                // Error occurred while creating the File
                Log.e(C.LOG_TAG, "Error occurred while creating Image File.");
            }
            tempUri = Uri.fromFile(imageFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(takePictureIntent,C.CAMERA_REQUEST);
            return imageFile;
        }else{
            return null;
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
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case C.CAMERA_REQUEST:
                if(resultCode == RESULT_OK){

                    ImageUtils.deleteImage(newImagePath);
                    newImagePath = cameraImagePath;
                    ImageUtils.saveNewBitmap(ImageUtils.rotateImage(newImagePath),newImagePath);
                    productImageFilePath.setText(newImagePath);

                }else if(resultCode == RESULT_CANCELED){

                    ImageUtils.deleteImage(cameraImagePath);
                }
                break;
            case C.GALLERY_REQUEST:
                if(resultCode == RESULT_OK){

                    String sourceFile = ImageUtils.getRealPathFromURI(this,data.getData());
                    try {
                        ImageUtils.deleteImage(newImagePath);
                        newImagePath = ImageUtils.createImageFile().getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageUtils.saveNewBitmap(ImageUtils.rotateImage(sourceFile),newImagePath);
                    productImageFilePath.setText(newImagePath);

                }
                break;
            case C.ASSOCIATE_PRODUCT_REQUEST:
                if(resultCode == RESULT_OK){
                    Snackbar.make(productCategoryEditText, getString(R.string.association_successful), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();

                }else if(resultCode == RESULT_CANCELED){
                    Snackbar.make(productCategoryEditText, getString(R.string.association_cancelled), Snackbar.LENGTH_LONG)
                            .setAction(C.ACTION, null).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Receiving call backs here from requestPermissions
     * @param requestCode the request code set when calling permissions request.
     * @param permissions the string array of permissions requested.
     * @param grantResults an int array of the granted results corresponding to the permissions string array
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case C.REQUEST_EXTERNAL_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    DialogUtils.showMultiUseDialog(EditProductActivity.this,C.IMAGE_DIALOG_TAG,R.string.image_dialog_message,R.string.image_from_file,C.NULL,R.string.image_from_camera);
                    break;
            case C.REQUEST_CAMERA_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    dispatchTakePictureIntent();
                   break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
                }
    }

    /**
     * Overriding onBackPressed to delete the new image file if it is not the same as the original image file.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!origImagePath.equals(newImagePath)) {
            ImageUtils.deleteImage(newImagePath);
        }
    }

}


