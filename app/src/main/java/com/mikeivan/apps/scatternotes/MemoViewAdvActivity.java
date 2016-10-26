package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;

/*
 * This activity shows an advanced view of an individual memo
 */
public class MemoViewAdvActivity extends AppCompatActivity {

    // Gives us access to memo db
    private MemoDbHelper mMemoDbHelper;

    // Variables for all fields of memo row
    int currentID;
    String currentTitle;
    String currentNotes;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_list_item_advanced);

        int target = getIntent().getIntExtra("MemoPosition", -1);

        mMemoDbHelper = new MemoDbHelper(this);
        SQLiteDatabase db = mMemoDbHelper.getReadableDatabase();


        String[] project = {
                MemoContract.MemoTableEntry._ID,
                MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE,
                MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES,
                MemoContract.MemoTableEntry.COLUMN_MEMO_DATE
        };

        String whereClause = MemoContract.MemoTableEntry._ID + "=?";
        String[] selectionArgs = {"" + target};

        Cursor cursor = db.query(MemoContract.MemoTableEntry.TABLE_NAME,
                project,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE);
            int notesColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_DATE);

            //Toast.makeText(getBaseContext(), "Query count: " + cursor.getCount(), Toast.LENGTH_SHORT).show();

            // Only set TextViews if something is found
            if (cursor.moveToNext() && cursor.getCount() > 0) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                currentTitle = cursor.getString(titleColumnIndex);
                currentNotes = cursor.getString(notesColumnIndex);
                currentDate = cursor.getString(dateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView


                TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
                titleTextView.setText(currentTitle);

                TextView notesTextView = (TextView) findViewById(R.id.notes_text_view);
                notesTextView.setText(currentNotes);

                TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
                dateTextView.setText(currentDate);

                // Only setup FAB if we found something - FAB to edit memo
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MemoViewAdvActivity.this, MemoEditActivity.class);
                        i.putExtra(MemoContract.MemoTableEntry._ID, currentID);
                        i.putExtra(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE, currentTitle);
                        i.putExtra(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES, currentNotes);
                        startActivity(i);
                        finish();
                    }
                });

            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }
}
