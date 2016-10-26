package com.mikeivan.apps.scatternotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mivan on 9/25/2016.
 *
 * This is an extension of SQLiteOpenHelper to create,
 * update, and delete the memo db.
 */

public class MemoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";

    public MemoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE memo (_id INTEGER, title TEXT, notes TEXT, date TEXT);
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + MemoContract.MemoTableEntry.TABLE_NAME + " (" +
                MemoContract.MemoTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE + " TEXT, "
                + MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES + " TEXT, "
                + MemoContract.MemoTableEntry.COLUMN_MEMO_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // This database is local only, so its upgrade policy is
    // to simply to discard the data and start over
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MemoContract.MemoTableEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // On downgrade just call upgrade.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
