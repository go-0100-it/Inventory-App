package com.waters89gmail.dave.totalinventorycontrol.object_classes;

import java.io.Serializable;

/**
 * Created by WatersD on 8/10/2016.
 */
public class Product implements Serializable{

    long mProductId;
    long mNumber;
    String mName;
    String mDescription;
    String mCategory;
    String mImageSource;
    double mPrice;
    String mType;
    int mQtyOnHand;


    public Product() {

        mProductId = 0; mNumber = 0; mName =""; mDescription = ""; mCategory = ""; mImageSource = ""; mPrice = 0.00; mType = "";
        mQtyOnHand = 0;
    }

    public Product(long mProductId, String mName, long mNumber, String mDescription, String mCategory, double mPrice, int mQtyOnHand, String mType, String mImageSource) {

        this.mProductId = mProductId; this.mNumber = mNumber; this.mName = mName; this.mDescription = mDescription; this.mCategory = mCategory;
        this.mImageSource = mImageSource; this.mPrice = mPrice; this.mType = mType; this.mQtyOnHand = mQtyOnHand;
    }

    public Product(String mName, long mNumber, String mDescription, String mCategory, double mPrice, int mQtyOnHand, String mType, String mImageSource) {

        this.mNumber = mNumber; this.mName = mName; this.mDescription = mDescription; this.mCategory = mCategory;
        this.mImageSource = mImageSource; this.mPrice = mPrice; this.mType = mType; this.mQtyOnHand = mQtyOnHand;
    }

    public long getmNumber() {
        return mNumber;
    }

    public void setmNumber(long mNumber) {
        this.mNumber = mNumber;
    }

    public long getmProductId() {return mProductId;}

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmImage() {
        return mImageSource;
    }

    public void setmImage(String mImage) {
        this.mImageSource = mImage;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public int getmQtyOnHand() {
        return mQtyOnHand;
    }

    public void setmQtyOnHand(int mQtyOnHand) {
        this.mQtyOnHand = mQtyOnHand;
    }
}
