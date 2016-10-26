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

import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

import java.util.ArrayList;

/*
 * This activity queries the exercise db and displays the results
 */
public class ExerciseSearchResultsActivity extends AppCompatActivity {

    // _id of workout for use with exercise db
    private int currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search_results);


        setResults();
    }

    /*
     * This function performs the query and sets the results
     */
    private void setResults() {
        // Retrieve the query string from intent
        String target = getIntent().getStringExtra("query");

        // Create database interaction objects
        ExerciseDbHelper mExerciseDbHelper = new ExerciseDbHelper(this);
        ArrayList<ExerciseEntry> workouts = new ArrayList<ExerciseEntry>();

        SQLiteDatabase db = mExerciseDbHelper.getReadableDatabase();

        String[] project = {
                ExerciseTableEntry._ID,
                ExerciseTableEntry.COLUMN_WORKOUT_PARENT,
                ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE,
                ExerciseTableEntry.COLUMN_WORKOUT_DATE
        };

        // Setup a cursor for the results of the query
        Cursor cursor = db.query(ExerciseTableEntry.TABLE_NAME,
                project,                               // The columns to return
                target,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
             // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry._ID);
            int exerciseColumnIndex = cursor.getColumnIndex(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE);
            int dateColumnIndex = cursor.getColumnIndex(ExerciseTableEntry.COLUMN_WORKOUT_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                String currentExercise = cursor.getString(exerciseColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                workouts.add(new ExerciseEntry(currentID, cursor.getString(exerciseColumnIndex), cursor.getString(dateColumnIndex)));

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        ExerciseAdapter adapter = new ExerciseAdapter(this, workouts);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Setup onclicklistener so individual exercises can be edited
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ExerciseEntry currentWorkout = (ExerciseEntry) parent.getItemAtPosition(position);

                //Toast.makeText(getBaseContext(),"Clicked: " + position + " workout: " + currentWorkout.getExercise(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ExerciseSearchResultsActivity.this, ExerciseEditActivity.class);
                i.putExtra(ExerciseContract.ExerciseTableEntry._ID, currentWorkout.getId());
                i.putExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, currentWorkout.getExercise());
                i.putExtra(ExerciseTableEntry.COLUMN_WORKOUT_DATE, currentWorkout.getDate());
                startActivity(i);
                finish();
            }
        });

    }
}
