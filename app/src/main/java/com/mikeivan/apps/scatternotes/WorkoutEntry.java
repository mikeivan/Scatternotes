package com.mikeivan.apps.scatternotes;

/**
 * Created by Mivan on 9/19/2016.
 *
 * This class is to be used with an adapter class for viewing workouts
 * in a scrollview
 */
public class WorkoutEntry {

    // Only a subset of the beer row is used for display/data for intents
    // _id of workout
    // date of table row creation
    private int mId;
    private String mDate;

    // Constructor
    public WorkoutEntry(int id, String date) {
        mId = id;
        mDate = date;
    }

    // Getter functions used by adapter
    public int getId() { return mId; }

    public String getDate() { return mDate; }

}
