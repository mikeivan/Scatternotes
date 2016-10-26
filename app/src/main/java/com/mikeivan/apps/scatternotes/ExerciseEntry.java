package com.mikeivan.apps.scatternotes;

/**
 * Created by Mivan on 9/21/2016.
 *
 * This class is to be used with an adapter class for viewing exercises
 * in a ScrollView
 */
public class ExerciseEntry {

    // Only a subset of the exercise row is used for display/data for intents
    // _id of exercise
    // exercise
    // date of parent creation
    private int mId;
    private String mExercise;
    private String mDate;

    // Constructor
    public ExerciseEntry(int id, String exercise, String date) {
        mId = id;
        mExercise = exercise;
        mDate = date;
    }

    // Getter functions used by adapter
    public int getId() { return mId; }

    public String getExercise() { return mExercise; }

    public String getDate() { return mDate; }

}
