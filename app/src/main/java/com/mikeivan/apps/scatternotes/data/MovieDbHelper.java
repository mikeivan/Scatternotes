package com.mikeivan.apps.scatternotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mivan on 9/19/2016.
 *
 * This is an extension of SQLiteOpenHelper to create,
 * update, and delete the movie db.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE pets (_id INTEGER, title TEXT, year INTEGER, rating INTEGER,
        //                   notes TEXT, date TEXT);
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + MovieContract.MovieTableEntry.TABLE_NAME + " (" +
                MovieContract.MovieTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieTableEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieContract.MovieTableEntry.COLUMN_MOVIE_YEAR + " INTEGER NOT NULL DEFAULT 2016, "
                + MovieContract.MovieTableEntry.COLUMN_MOVIE_RATING + " INTEGER NOT NULL DEFAULT 0, "
                + MovieContract.MovieTableEntry.COLUMN_MOVIE_NOTES + " TEXT,"
                + MovieContract.MovieTableEntry.COLUMN_MOVIE_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // This database is local only, so its upgrade policy is
    // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MovieContract.MovieTableEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // On downgrade just call upgrade.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
