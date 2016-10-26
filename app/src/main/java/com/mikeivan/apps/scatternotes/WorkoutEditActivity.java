package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.WorkoutContract.WorkoutTableEntry;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;
import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

import java.util.ArrayList;

/*
 * This activity is used to update or delete a workout row.
 */
public class WorkoutEditActivity extends AppCompatActivity {

    // TextView for date of workout
    private TextView mDateTextView;

    // _id of workout for use with exercise db
    private int currentID;

    /*
     * This function deletes all exercise rows associated with the workout
     * and then deletes the workout from the workout database
     */
    private void deleteWorkout() {
        // Create database interaction objects for workout db
        WorkoutDbHelper mDbHelper = new WorkoutDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create database interaction objects for exercise db
        ExerciseDbHelper mExerciseDbHelper = new ExerciseDbHelper(this);
        SQLiteDatabase db2 = mExerciseDbHelper.getWritableDatabase();


        String whereClause = WorkoutTableEntry._ID + "=?";
        String[] selectionArgs = {"" + getIntent().getIntExtra(WorkoutTableEntry._ID, -1)};

        //delete all exercise entries with parentID = _ID first
        String whereClause2 = ExerciseTableEntry.COLUMN_WORKOUT_PARENT + "=?";

        // delete the exercise row(s), returning the number of rows deleted
        long delRowId = db2.delete(ExerciseContract.ExerciseTableEntry.TABLE_NAME, whereClause2, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if (delRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Exercises deleted: " + delRowId, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getApplicationContext(), "Error with deleting exercises", Toast.LENGTH_SHORT).show();
            Log.v("WorkoutEditActivity", "Error with deleting exercise(s)");
        }


        // delete the workout row, returning the number of rows deleted (should be 1 or 0)
        long newRowId = db.delete(WorkoutTableEntry.TABLE_NAME, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if (newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Workout deleted: " + newRowId, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getApplicationContext(), "Error with deleting workout", Toast.LENGTH_SHORT).show();
            Log.v("WorkoutEditActivity", "Error with deleting workout");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_edit);

        // Setup FAB to open add exercise to workout
        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WorkoutEditActivity.this, ExerciseAddActivity.class);
                i.putExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT, getIntent().getIntExtra(WorkoutTableEntry._ID, -1));
                i.putExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE, getIntent().getStringExtra(WorkoutTableEntry.COLUMN_GYM_DATE));
                startActivity(i);
                finish();
            }
        });

        // Setup FAB to delete workout (and corresponding exercises)
        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWorkout();
                Intent i = new Intent(WorkoutEditActivity.this, WorkoutViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup TextViews
        mDateTextView = (TextView) findViewById(R.id.gym_calendar);

        // Populate EditTexts with actual values from the db, this should make editing easier
        mDateTextView.setText(getIntent().getStringExtra(WorkoutTableEntry.COLUMN_GYM_DATE));

        //query exercise db and appened to this textview, eventually update to adapter
        //probably simple one will work with a string object

        ArrayList<ExerciseEntry> workouts = new ArrayList<ExerciseEntry>();

        ExerciseDbHelper mExerciseDbHelper = new ExerciseDbHelper(this);
        SQLiteDatabase db = mExerciseDbHelper.getReadableDatabase();

        String[] project = {
                ExerciseContract.ExerciseTableEntry._ID,
                ExerciseTableEntry.COLUMN_WORKOUT_PARENT,
                ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE,
                ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE
        };

        String whereClause = ExerciseTableEntry.COLUMN_WORKOUT_PARENT + "=?";
        String[] selectionArgs = {"" + getIntent().getIntExtra(WorkoutTableEntry._ID, -1)};

        final Cursor cursor = db.query(ExerciseContract.ExerciseTableEntry.TABLE_NAME,
                project,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try {
            int idColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry._ID);
            int exerciseColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE);
            int dateColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE);

            while (cursor.moveToNext()) {
                //assign listeners?
                currentID = cursor.getInt(idColumnIndex);
                // add exercises to arraylist for use with adapter
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

                Intent i = new Intent(WorkoutEditActivity.this, ExerciseEditActivity.class);
                i.putExtra(ExerciseContract.ExerciseTableEntry._ID, currentWorkout.getId());
                i.putExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, currentWorkout.getExercise());
                i.putExtra(ExerciseTableEntry.COLUMN_WORKOUT_DATE, currentWorkout.getDate());
                startActivity(i);
                finish();
            }
        });

    }

}
