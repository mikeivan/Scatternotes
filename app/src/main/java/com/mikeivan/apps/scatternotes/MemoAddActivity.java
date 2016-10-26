package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;

/*
 * This activity adds a memo into the database.
 */
public class MemoAddActivity extends AppCompatActivity {

    // EditText field to enter memo's title
    private EditText mTitleEditText;

    // EditText field to enter memo's notes
    private EditText mNotesEditText;

    /*
     * This function adds a new memo into the memo database
     * date used is the date of creation.
     */
    private void insertMemo() {
        // Retrieve information in EditTexts
        String titleString = mTitleEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();

        // Create database interaction objects
        MemoDbHelper mDbHelper = new MemoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Deprecated class, but using for API 15 compatibility
        Time now = new Time();
        now.setToNow();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE, titleString);
        values.put(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES, notesString);
        values.put(MemoContract.MemoTableEntry.COLUMN_MEMO_DATE, now.format("%m-%d-%Y"));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MemoContract.MemoTableEntry.TABLE_NAME, null, values);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Memo saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Memo with saving exercise", Toast.LENGTH_SHORT).show();
            Log.v("MemoAddActivity", "Error with saving memo");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);

        // Setup FAB to save memo into database
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMemo();
                Intent i = new Intent(MemoAddActivity.this, MemoViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mTitleEditText = (EditText) findViewById(R.id.edit_title);
        mNotesEditText = (EditText) findViewById(R.id.edit_notes);

    }
}
