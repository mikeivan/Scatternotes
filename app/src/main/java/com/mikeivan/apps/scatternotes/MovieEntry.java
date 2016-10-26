package com.mikeivan.apps.scatternotes;

/**
 * Created by Mivan on 9/19/2016.
 *
 * This class is to be used with an adapter class for viewing movies
 * in a scrollview
 */
public class MovieEntry {

    // Only a subset of the movie row is used for display/data for intents
    // _id of movie
    // title of movie
    // date of table row creation
    private int mId;
    private String mMovie;
    private String mDate;

    // Constructor
    public MovieEntry(int id, String movie, String date) {
        mId = id;
        mMovie = movie;
        mDate = date;
    }

    // Getter functions used by adapter
    public int getId() { return mId; }

    public String getMovie() {
        return mMovie;
    }

    public String getDate() { return mDate; }

}
