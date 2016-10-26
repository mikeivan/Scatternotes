package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;

import java.util.ArrayList;

/*
 * This activity displays the beers in the database
 */
public class MemoViewActivity extends AppCompatActivity {

    // Gives us access to memo db
    private MemoDbHelper mMemoDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        mMemoDbHelper = new MemoDbHelper(this);
        ArrayList<MemoEntry> memos = new ArrayList<MemoEntry>();

        SQLiteDatabase db = mMemoDbHelper.getReadableDatabase();

        String[] project = {
                MemoContract.MemoTableEntry._ID,
                MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE,
                MemoContract.MemoTableEntry.COLUMN_MEMO_DATE
        };

        Cursor cursor = db.query(MemoContract.MemoTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Create a new MemoEntry for each row
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

                Intent i = new Intent(MemoViewActivity.this, MemoViewAdvActivity.class);
                i.putExtra("MemoPosition", currentMemo.getId());
                startActivity(i);
                finish();
            }
        });

    }
}
