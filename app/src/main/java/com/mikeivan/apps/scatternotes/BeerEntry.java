package com.mikeivan.apps.scatternotes;

/**
 * Created by Mivan on 9/7/2016.
 *
 * This class is to be used with an adapter class for viewing beers
 * in a ScrollView
 */
public class BeerEntry {

    // Only a subset of the beer row is used for display/data for intents
    // _id of beer
    // company of beer
    // product of beer
    // date of table row creation
    private int mId;
    private String mCompany;
    private String mProduct;
    private String mDate;

    // Constructor
    public BeerEntry(int id, String company, String product, String date) {
        mId = id;
        mCompany = company;
        mProduct = product;
        mDate = date;
    }

    // Getter functions used by adapter
    public int getId() { return mId; }

    public String getCompany() {
        return mCompany;
    }

    public String getProduct() {
        return mProduct;
    }

    public String getDate() { return mDate; }

}
