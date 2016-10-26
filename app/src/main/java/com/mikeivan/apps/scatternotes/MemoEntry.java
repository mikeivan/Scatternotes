package com.mikeivan.apps.scatternotes;

/**
 * Created by Mivan on 9/25/2016.
 *
 * This class is to be used with an adapter class for viewing exercises
 * in a scrollview
 */
public class MemoEntry {

    // Only a subset of the memo row is used for display/data for intents
    // _id of memo
    // title of memo
    // date of table row creation
    private int mId;
    private String mTitle;
    private String mDate;

    // Constructor
    public MemoEntry(int id, String title, String date) {
        mId = id;
        mTitle = title;
        mDate = date;
    }

    // Getter functions used by adapter
    public int getId() { return mId; }

    public String getTitle() { return mTitle; }

    public String getDate() { return mDate; }

}