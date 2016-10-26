package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/*
 * This activity queries the memo db and displays the results
 */
public class MemoSearchResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_search_results);


        setResults();

    }

    /*
     * This function performs the query and sets the results
     */
    private void setResults() {
        // Retrieve the query string from intent
        String target = getIntent().getStringExtra("query");

        // Create database interaction objects
        MemoDbHelper mMemoDbHelper = new MemoDbHelper(this);
        ArrayList<MemoEntry> memos = new ArrayList<MemoEntry>();

        SQLiteDatabase db = mMemoDbHelper.getReadableDatabase();

        String[] project = {
                MemoContract.MemoTableEntry._ID,
                MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE,
                MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES,
                MemoContract.MemoTableEntry.COLUMN_MEMO_DATE
        };

        // Setup a cursor for the results of the query
        Cursor cursor = db.query(MemoContract.MemoTableEntry.TABLE_NAME,
                project,                               // The columns to return
                target,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
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

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                memos.add(new MemoEntry(currentID, currentTitle, currentDate));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // Create a MemoAdapter for our memo array
        MemoAdapter adapter = new MemoAdapter(this, memos);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Create a clicklistener so when an entry is clicked we are taken to
        // the advanced view of that item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MemoEntry currentMemo = (MemoEntry) parent.getItemAtPosition(position);

                //Toast.makeText(getBaseContext(),"Clicked: " + position + " memo: " + currentMemo.getTitle(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MemoSearchResultsActivity.this, MemoViewAdvActivity.class);
                i.putExtra("MemoPosition", currentMemo.getId());
                startActivity(i);
                finish();
            }
        });

    }
}
