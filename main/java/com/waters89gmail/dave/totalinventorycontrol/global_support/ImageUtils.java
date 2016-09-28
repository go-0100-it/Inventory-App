package com.waters89gmail.dave.totalinventorycontrol.global_support;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils extends AppCompatActivity {

    public ImageUtils(){}

    /**
     * A method to rotate a bitmap image if required for display.
     * @param path the file path of the bitmap to rotate.
     * @return returns a correctly orientated bitmap.
     */
    public static Bitmap rotateImage(final String path) {

        File file = new File(path);

        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitMap = BitmapFactory.decodeStream(streamIn);

        Bitmap rotatedBitmap;
        try {
            ExifInterface ei = new ExifInterface(path);
            //getting images orientation details
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(bitMap, C.NULL, C.NULL,
                            bitMap.getWidth(), bitMap.getHeight(),
                            matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    rotatedBitmap = Bitmap.createBitmap(bitMap, C.NULL, C.NULL,
                            bitMap.getWidth(), bitMap.getHeight(),
                            matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    rotatedBitmap = Bitmap.createBitmap(bitMap, C.NULL, C.NULL,
                            bitMap.getWidth(), bitMap.getHeight(),
                            matrix, true);
                    break;
                default:
                    rotatedBitmap = Bitmap.createBitmap(bitMap, C.NULL, C.NULL,
                            bitMap.getWidth(), bitMap.getHeight(),
                            matrix, true);
                    break;
            }
            return rotatedBitmap;
        } catch (Throwable e) {
            e.printStackTrace();
            return bitMap;
        }
    }

    /**
     * A method to create a new temp file with a unique name.
     * @return returns a new file
     * @throws IOException
     */
    public static File createImageFile() throws IOException {
        // Create a new image file name
        String timeStamp = new SimpleDateFormat(C.DATE_FORMAT, Locale.getDefault()).format(new Date());
        String imageFileName = C.JPEG + C.UNDER_SCORE + timeStamp + C.UNDER_SCORE;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                C.DOT_JPG,         /* suffix */
                storageDir      /* directory */);
    }

    /**
     * A method to get a file path from a URI
     * @param context needed to access the ContentResolver.
     * @param contentUri the Uri to get the file path of.
     * @return returns a string value representing the file path of the Uri passed in.
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * A method to scale, compress and save a bitmap to a file.
     * @param bitmap the bitmap to scale, compress and save.
     * @param path the file path where to save the bitmap.
     * @return returns the scaled bitmap.
     */
    public static Bitmap saveNewBitmap(Bitmap bitmap,String path){

        //constructs a File object to save the scaled file to from path arg.
        File file = new File(path);

        if (bitmap != null) {
            //gets the original image dimensions
            int bmOriginalWidth = bitmap.getWidth();
            int bmOriginalHeight = bitmap.getHeight();
            double originalWidthToHeightRatio = 1.0 * bmOriginalWidth / bmOriginalHeight;
            double originalHeightToWidthRatio = 1.0 * bmOriginalHeight / bmOriginalWidth;
            //setting a maximum height
            int maxHeight = 500;
            //setting a maximum width
            int maxWidth = 250;
            //calls the method to get the scaled bitmap
            bitmap = getScaledBitmap(bitmap, bmOriginalWidth, bmOriginalHeight,
                    originalWidthToHeightRatio, originalHeightToWidthRatio,
                    maxHeight, maxWidth);

            //creates a byte array output stream to hold the photo's bytes
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //compresses the photo's bytes into the byte array output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            //creates an FileOutputStream on the created file
            //writes the photo's bytes to the file
            //finishes by closing the FileOutputStream
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * A method to determine how the bitmap should be scaled.
     * @param bitmap the bitmap to scale.
     * @param bitmapOrigWidth the original width of the bitmap to be scaled.
     * @param bitmapOrigHeight the original height of the bitmap to be scaled.
     * @param origWidthToHeightRatio the original width to height ratio of the bitmap to be scaled.
     * @param origHeightToWidthRatio the original height to width ratio of the bitmap to be scaled.
     * @param maxHeight the maximum height for the scaled bitmap to be.
     * @param maxWidth the maximum width for the scaled bitmap to be.
     * @return returns a bitmap scaled to original ratios
     */
    private static Bitmap getScaledBitmap(Bitmap bitmap, int bitmapOrigWidth, int bitmapOrigHeight, double origWidthToHeightRatio,
                                          double origHeightToWidthRatio, int maxHeight, int maxWidth) {
        if(bitmapOrigWidth > maxWidth || bitmapOrigHeight > maxHeight) {

            //
            if(bitmapOrigWidth > bitmapOrigHeight) {
                bitmap = scaleDimensFromWidth(bitmap, maxWidth, origHeightToWidthRatio);
            } else if (bitmapOrigHeight > bitmapOrigWidth){
                bitmap = scaleDimensFromHeight(bitmap, maxHeight, origWidthToHeightRatio);
            }
        }
        return bitmap;
    }

    /**
     * A method to scale a bitmap by setting the height.
     * @param bm the bitmap to scale.
     * @param maxHeight the value to set the height to.
     * @param origWidthToHeightRatio the bitmaps original width to height ratio.
     * @return returns a scale bitmap
     */
    private static Bitmap scaleDimensFromHeight(Bitmap bm, int maxHeight, double origWidthToHeightRatio) {
        //scales the width to the passed in height value.
        int newWidth = (int) (maxHeight * origWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, maxHeight, true);
        return bm;
    }

    /**
     * A method to scale a bitmap by setting the width.
     * @param bm the bitmap to scale.
     * @param maxWidth the value to set the width to.
     * @param origHeightToWidthRatio the bitmaps original height to width ratio.
     * @return returns a scale bitmap
     */
    private static Bitmap scaleDimensFromWidth(Bitmap bm, int maxWidth, double origHeightToWidthRatio) {
        //scales the height to the passed in width value.
        int newHeight = (int) (maxWidth * origHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, maxWidth, newHeight, true);
        return bm;
    }

    /**
     * A mehtod to delete a image file if it exists.
     * @param path the file path of the image to delete.
     * @return returns true if the file was deleted otherwise returns false.
     */
    public static boolean deleteImage(String path){
        if (path != null && !path.equals(C.EMPTY_STRING)){
            File file = new File(path);
            return file.delete();
        }
        return false;
    }
}
