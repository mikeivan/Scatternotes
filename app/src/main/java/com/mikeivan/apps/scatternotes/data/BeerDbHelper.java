package com.mikeivan.apps.scatternotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;

/**
 * Created by Mivan on 9/13/2016.
 *
 * This is an extension of SQLiteOpenHelper to create,
 * update, and delete the beer db.
 */
public class BeerDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";

    public BeerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE beer (_id INTEGER, company TEXT, product TEXT, rating INTEGER,
        //                   notes TEXT, date TEXT);
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + BeerTableEntry.TABLE_NAME + " (" +
                BeerTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BeerTableEntry.COLUMN_BEER_COMPANY + " TEXT, "
                + BeerTableEntry.COLUMN_BEER_PRODUCT + " TEXT NOT NULL, "
                + BeerTableEntry.COLUMN_BEER_RATING + " INTEGER NOT NULL DEFAULT 0, "
                + BeerTableEntry.COLUMN_BEER_NOTES + " TEXT,"
                + BeerTableEntry.COLUMN_BEER_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }


     // This database is local only, so its upgrade policy is
     // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BeerTableEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // On downgrade just call upgrade.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
