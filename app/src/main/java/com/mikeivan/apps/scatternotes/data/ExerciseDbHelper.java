package com.mikeivan.apps.scatternotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mivan on 9/21/2016.
 *
 * This is an extension of SQLiteOpenHelper to create,
 * update, and delete the exercise db.
 */

public class ExerciseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE exercise (_id INTEGER, parent_id INTEGER, exercise TEXT, date TEXT);
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + ExerciseContract.ExerciseTableEntry.TABLE_NAME + " (" +
                ExerciseContract.ExerciseTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT + " INTEGER NOT NULL, "
                + ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE + " TEXT NOT NULL, "
                + ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // This database is local only, so its upgrade policy is
    // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ExerciseContract.ExerciseTableEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // On downgrade just call upgrade.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
