package com.waters89gmail.dave.totalinventorycontrol.product_activity;

import java.io.Serializable;

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

    public long getmProductId() {return mProductId;}

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmImage() {
        return mImageSource;
    }

    public double getmPrice() {
        return mPrice;
    }

    public String getmType() {
        return mType;
    }

    public int getmQtyOnHand() {
        return mQtyOnHand;
    }


    public void setmQtyOnHand(int mQtyOnHand) {
        this.mQtyOnHand = mQtyOnHand;
    }
}
