package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;

/*
 * This activity is used to update or delete a memo row.
 */
public class MemoEditActivity extends AppCompatActivity {


    // EditText field to enter the memo's title
    private EditText mTitleEditText;


    // EditText field to enter the memo's notes
    private EditText mNotesEditText;

    /*
     * This function updates a memo in the memo database
     * date is unaltered - still date of creation
     */
    private void updateMemo() {
        // Retrieve information in EditTexts
        String titleString = mTitleEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();

        // Create database interaction objects
        MemoDbHelper mDbHelper = new MemoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE, titleString);
        values.put(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES, notesString);

        // Create the where clause, so we update only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = MemoContract.MemoTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(MemoContract.MemoTableEntry._ID, -1) };

        // Update the row, returning the number of rows updated (should be 1 or 0)
        long newRowId = db.update(MemoContract.MemoTableEntry.TABLE_NAME, values, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Memo saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with saving memo", Toast.LENGTH_SHORT).show();
            Log.v("MemoEditActivity", "Error with updating memo");
        }
    }

    /*
     * This function deletes a memo row from the memo database
     */
    private void deleteMemo() {
        // Create database interaction objects
        MemoDbHelper mDbHelper = new MemoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create the where clause, so we delete only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = MemoContract.MemoTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(MemoContract.MemoTableEntry._ID, -1) };

        // delete the row, returning the number of rows deleted (should be 1 or 0)
        long newRowId = db.delete(MemoContract.MemoTableEntry.TABLE_NAME, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Memo deleted: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with deleting memo", Toast.LENGTH_SHORT).show();
            Log.v("MemoEditActivity", "Error with deleting memo");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        // Setup FAB to update memo
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMemo();
                Intent i = new Intent(MemoEditActivity.this, MemoViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup FAB to delete memo
        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMemo();
                Intent i = new Intent(MemoEditActivity.this, MemoViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mTitleEditText = (EditText) findViewById(R.id.edit_memo_title);
        mNotesEditText = (EditText) findViewById(R.id.edit_memo_notes);

        // Populate EditTexts with actual values from the db, this should make editing easier
        mTitleEditText.setText(getIntent().getStringExtra(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE));
        mNotesEditText.setText(getIntent().getStringExtra(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES));
    }
}
