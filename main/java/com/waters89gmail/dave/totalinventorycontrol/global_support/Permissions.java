package com.waters89gmail.dave.totalinventorycontrol.global_support;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.waters89gmail.dave.totalinventorycontrol.R;

public class Permissions extends AppCompatActivity{

    public Permissions(){}

    /**
     * A method to check if Camera permissions were granted and if not call method to request permissions or show additional permission rationale.
     * @param activity the activity requesting the confirmation.  To be passed to downstream methods, required for returning results if permissions are requested.
     * @return returns true if permissions were granted else returns false.
     */
    public static boolean confirmCameraPermissions(AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            showCameraPermissionRational(activity);
            return false;
        }
    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a Dialog will give the user additional rationale to grant the
     * permission, otherwise it is requested directly.
     */
    private static void showCameraPermissionRational(AppCompatActivity activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                C.CAMERA_PERMISSIONS[0])) {
            // Provide an additional rationale to the user if the permission was not granted
            DialogUtils.showMultiUseDialog(activity,C.CAMERA_PERMISSIONS_DIALOG,R.string.camera_permissions_rational,C.NULL,C.NULL,R.string.ok);

        } else {
            // Camera permission has not been granted yet. Request it directly.
            requestCameraPermissions(activity);
        }
    }

    /**
     * A method to request Camera permissions.  To be called after checking if permissions were previously granted and, if necessary, after showing additional rationale.
     * @param activity the activity requesting the permissions.
     */
    public static void requestCameraPermissions(AppCompatActivity activity){
        ActivityCompat.requestPermissions(activity,C.CAMERA_PERMISSIONS,C.REQUEST_CAMERA_PERMISSIONS);
    }

    /**
     * A method to check if Phone permissions were granted and if not call method to request permissions or show additional permission rationale.
     * @param activity the activity requesting the confirmation.  To be passed to downstream methods, required for returning results if permissions are requested.
     * @return returns true if permissions were granted else returns false.
     */
    public static boolean confirmPhonePermissions(AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            showPhonePermissionRationale(activity);
            return false;
        }
    }

    /**
     * Requests the Phone permission.
     * If the permission has been denied previously, a Dialog will give the user additional rationale to grant the
     * permission, otherwise it is requested directly.
     */
    private static void showPhonePermissionRationale(AppCompatActivity activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                C.CALL_PERMISSIONS[0])) {
            // Providing additional rationale to the user if the permission was not granted
            DialogUtils.showMultiUseDialog(activity,C.PHONE_PERMISSIONS_DIALOG,R.string.phone_permissons_rational,C.NULL,C.NULL,R.string.ok);

        } else {
            // Phone permission has not been granted yet. Request it directly.
           requestPhonePermission(activity);
        }
    }

    /**
     * A method to request Phone permissions.  To be called after checking if permissions were previously granted and, if necessary, after showing additional rationale.
     * @param activity the activity requesting the permissions.
     */
    public static void requestPhonePermission(AppCompatActivity activity){
        ActivityCompat.requestPermissions(activity,C.CALL_PERMISSIONS,
                C.REQUEST_CALL_PERMISSIONS);
    }

    /**
     * A method to check if Storage permissions were granted and if not call method to request permissions or show additional permission rationale.
     * @param activity the activity requesting the confirmation.  To be passed to downstream methods, required for returning results if permissions are requested.
     * @return returns true if permissions were granted else returns false.
     */
    public static boolean confirmStoragePermissions(AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            showStoragePermissionsRationale(activity);
            return false;
        }
    }

    /**
     * Requests the Storage permissions.
     * If the permission has been denied previously, a Dialog will give the user additional rationale to grant the
     * permission, otherwise it is requested directly.
     */
    private static void showStoragePermissionsRationale(AppCompatActivity activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            DialogUtils.showMultiUseDialog(activity,C.STORAGE_PERMISSIONS_DIALOG,R.string.storage_permissions_rational,C.NULL,C.NULL,R.string.ok);

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            requestStoragePermissions(activity);
        }
    }

    /**
     * A method to request Storage permissions.  To be called after checking if permissions were previously granted and, if necessary, after showing additional rationale.
     * @param activity the activity requesting the permissions.
     */
    public static void requestStoragePermissions(AppCompatActivity activity){
        ActivityCompat.requestPermissions(activity, C.PERMISSIONS_STORAGE, C.REQUEST_EXTERNAL_STORAGE);
    }
}
