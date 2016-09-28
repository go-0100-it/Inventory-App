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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.DialogUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.ImageUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;

import java.io.File;
import java.io.IOException;

/**
 * An activity to edit a product in the database or to add a new product to the database.
 */
public class AddProductActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback, MultiUseDialog.MultiUseDialogListener {

    private String origImagePath = C.EMPTY_STRING;
    private String cameraImagePath = C.EMPTY_STRING;
    private String newImagePath = C.EMPTY_STRING;

    private EditText productNameEditText, productNumberEditText, productDescriptionEditText, productPriceEditText, quantityOnHandEditText, productImageFilePath;
    private Spinner productCategoryEditText, productTypeEditText;

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_product_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        productNumberEditText = (EditText) findViewById(R.id.product_number_edit_text);
        productDescriptionEditText = (EditText) findViewById(R.id.product_description_edit_text);
        productCategoryEditText = (Spinner) findViewById(R.id.product_category_edit_text);
        productPriceEditText = (EditText) findViewById(R.id.price_edit_text);
        quantityOnHandEditText = (EditText) findViewById(R.id.quantity_on_hand_edit_text);
        productTypeEditText = (Spinner) findViewById(R.id.type_edit_text);
        productImageFilePath = (EditText) findViewById(R.id.product_image_path);

        imageButton = (ImageButton) findViewById(R.id.imageBtn);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setTitle(getString(R.string.new_product_detail_activity));

        if (fab != null) {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addActivity(view);
                }
            });
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Checking if Storage permissions were are granted.  If permissions are granted call method to show image dialog.  If not, start permissions loop again.
        if(Permissions.confirmStoragePermissions(AddProductActivity.this)) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showMultiUseDialog(AddProductActivity.this,C.IMAGE_DIALOG_TAG,R.string.image_dialog_message,R.string.image_from_file,0,R.string.image_from_camera);
                }
            });
        }else{
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Permissions.confirmStoragePermissions(AddProductActivity.this);
                }
            });
        }
    }

    /**
     * Core function of this activity.
     * @param view The snackBar requires a view as a reference to a parent view
     */
    private void addActivity(View view) {

        if (verifyDataEntry(view)) {

            if (getContentResolver().insert(DataBaseProvider.PRODUCTS_URI, DataBaseUtils.createProductContentValues(createProduct())) != null) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            } else {
                Snackbar.make(view, getString(R.string.add_product_failed), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
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
                                    viewToFocus = imageButton;
                                    messageRes = R.string.add_product_needs_image;
                                }
                            }else{
                                viewToFocus = productTypeEditText;
                                messageRes = R.string.add_product_needs_type;
                            }
                        }else{
                            viewToFocus = productPriceEditText;
                            messageRes = R.string.add_product_needs_price;
                        }
                    }else{
                        viewToFocus = productCategoryEditText;
                        messageRes = R.string.add_product_needs_category;
                    }
                }else{
                    viewToFocus = productDescriptionEditText;
                    messageRes = R.string.add_product_needs_description;
                }
            }else{
                viewToFocus = productNumberEditText;
                messageRes = R.string.add_product_needs_number;
            }
        }else{
            viewToFocus = productNameEditText;
            messageRes = R.string.add_product_needs_name;
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
                        if (fieldToFocus == (productCategoryEditText)) {
                            productCategoryEditText.performClick();
                        } else if (fieldToFocus == productTypeEditText) {
                            productTypeEditText.performClick();
                        } else if (fieldToFocus == imageButton) {
                            imageButton.performClick();
                        } else
                            fieldToFocus.requestFocus();
                    }
                })
                .show();
    }

    /**
     * @return returns a new product created from the data extracted from the Views
     */
    private Product createProduct() {
        return new Product(productNameEditText.getText().toString(),
                Long.valueOf(productNumberEditText.getText().toString()),
                productDescriptionEditText.getText().toString(),
                productCategoryEditText.getSelectedItem().toString(),
                Double.valueOf(productPriceEditText.getText().toString()),
                Integer.valueOf(quantityOnHandEditText.getText().toString()),
                productTypeEditText.getSelectedItem().toString(),
                productImageFilePath.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (!origImagePath.equals(newImagePath)) {
                ImageUtils.deleteImage(newImagePath);
            }
            navigateUpTo(new Intent(this, ProductListFragment.class));
            return true;
        } else if (id == R.id.action_add_edit_association) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A method to start a photoPickerIntent
     */
    private void dispatchChoosePictureEvent(){
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
            // Create the File to pass to the Camera Intent, this is where the created photo will be saved.
            File imageFile = null;
            try {
                imageFile = ImageUtils.createImageFile();
                cameraImagePath = imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
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
     * @param requestCode returns the request code set when starting the intent, so we know which intent is returning the results
     * @param resultCode returns either RESULT_OK or RESULT_CANCELLED, RESULT_OK is returned if the desired actions have occurred or if the desired data is being return.
     * @param data returns the intent data containing the extras passed back from the activity started for results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            // if results are being returned from the takePicture Intent.
            case C.CAMERA_REQUEST:
                if(resultCode == RESULT_OK){
                    // Deleting the new image file if it exists.
                    // This is necessary if the user already captured an image via the camera and decided to capture a different image without saving the Product.
                    ImageUtils.deleteImage(newImagePath);
                    // setting the variable to the image captured via the camera.
                    newImagePath = cameraImagePath;
                    // Scaling and rotating the image if necessary and then re-saving to same file.
                    ImageUtils.saveNewBitmap(ImageUtils.rotateImage(newImagePath),newImagePath);
                    productImageFilePath.setText(newImagePath);

                }else if(resultCode == RESULT_CANCELED){
                    ImageUtils.deleteImage(cameraImagePath);
                }
                break;
            // if results are being returned from the photoPicker Intent.
            case C.GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    String sourceFile = ImageUtils.getRealPathFromURI(this,data.getData());
                    try {
                        // Deleting the new image file if it exists.
                        // This is necessary if the user already chose an image and decided to chose a different image without saving the Product.
                        ImageUtils.deleteImage(newImagePath);
                        // Creating a new file to save image to.
                        newImagePath = ImageUtils.createImageFile().getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Scaling and rotating the image if necessary and then saving image to the newly created file.
                    ImageUtils.saveNewBitmap(ImageUtils.rotateImage(sourceFile),newImagePath);
                    productImageFilePath.setText(newImagePath);
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
            // If call back is from Requesting storage permissions
            case C.REQUEST_EXTERNAL_STORAGE:
                // If Storage permissions were granted
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Calling method to show dialog to present options to user.  Call backs are handled @onDialogPositiveClick, @onDialogNeutralClick, @onDialogNegativeClick
                            DialogUtils.showMultiUseDialog(AddProductActivity.this,C.IMAGE_DIALOG_TAG,R.string.image_dialog_message,R.string.image_from_file,C.NULL,R.string.image_from_camera);
                        }
                    });
                break;
            // If call back is from Requesting camera permissions
            case C.REQUEST_CAMERA_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Calling method to start TakePictureIntent and setting file path to variable for downstream reference.
                   dispatchTakePictureIntent();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Handling positive button click call back from add image dialog.
        dispatchChoosePictureEvent();
    }

    /**
     * Overriding custom onClickListener interface to handle dialog neutral button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            // Handling negative button click call back from add image dialog.
            case C.IMAGE_DIALOG_TAG:
                // Checking Camera permissions before calling method to start a TakePictureIntent
                if(Permissions.confirmCameraPermissions(this)) {
                    //Calling method to start TakePictureIntent and setting file path to variable for downstream reference.
                    dispatchTakePictureIntent();
                }
                break;
            // Handling negative button click call back from storage permissions rationale dialog.
            case C.STORAGE_PERMISSIONS_DIALOG:
                Permissions.requestStoragePermissions(this);
                break;
            // Handling negative button click call back from camera permissions rationale dialog.
            case C.CAMERA_PERMISSIONS_DIALOG:
                Permissions.requestCameraPermissions(this);
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


