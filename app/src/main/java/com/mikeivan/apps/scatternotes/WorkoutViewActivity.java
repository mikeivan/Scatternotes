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

import com.mikeivan.apps.scatternotes.data.WorkoutContract.WorkoutTableEntry;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;

import java.util.ArrayList;

/*
 * This activity displays the workouts in the database
 */
public class WorkoutViewActivity extends AppCompatActivity {

    // Gives us access to workout db
    private WorkoutDbHelper mWorkoutDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        mWorkoutDbHelper = new WorkoutDbHelper(this);
        ArrayList<WorkoutEntry> workouts = new ArrayList<WorkoutEntry>();

        SQLiteDatabase db = mWorkoutDbHelper.getReadableDatabase();

        String[] project = {
                WorkoutTableEntry._ID,
                WorkoutTableEntry.COLUMN_GYM_DATE
        };

        Cursor cursor = db.query(WorkoutTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(WorkoutTableEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(WorkoutTableEntry.COLUMN_GYM_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                workouts.add(new WorkoutEntry(currentID, currentDate));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // Create a WorkoutAdapter for our workout array
        WorkoutAdapter adapter = new WorkoutAdapter(this, workouts);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Create a clicklistener so when an entry is clicked we are taken to
        // the advanced view of that item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                WorkoutEntry currentGym = (WorkoutEntry) parent.getItemAtPosition(position);

                //Toast.makeText(getBaseContext(),"Clicked: " + position + " workout: " + currentGym.getDate(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(WorkoutViewActivity.this, WorkoutViewAdvActivity.class);
                i.putExtra("GymPosition", currentGym.getId());
                startActivity(i);
                finish();
            }
        });

    }

}
