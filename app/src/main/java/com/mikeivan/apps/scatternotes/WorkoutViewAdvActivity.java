package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.WorkoutContract.WorkoutTableEntry;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;
import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

/*
 * This activity shows an advanced view of an individual workout
 * and a brief view of all associated exercises
 */
public class WorkoutViewAdvActivity extends AppCompatActivity {

    // Gives us access to workout db and exercise db
    private WorkoutDbHelper mWorkoutDbHelper;
    private ExerciseDbHelper mExerciseDbHelper;

    // Variables for all fields of workout row
    int currentID;
    String currentDate;

    // Variable for id of exercise
    int currentInnerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gym_list_item_advanced);

        int target = getIntent().getIntExtra("GymPosition", -1);

        mWorkoutDbHelper = new WorkoutDbHelper(this);
        SQLiteDatabase db = mWorkoutDbHelper.getReadableDatabase();

        mExerciseDbHelper = new ExerciseDbHelper(this);
        SQLiteDatabase db2 = mExerciseDbHelper.getReadableDatabase();

        String[] project = {
                WorkoutTableEntry._ID,
                WorkoutTableEntry.COLUMN_GYM_DATE
        };

        String whereClause = WorkoutTableEntry._ID + "=?";
        String[] selectionArgs = { "" + target };

        final Cursor cursor = db.query(WorkoutTableEntry.TABLE_NAME,
                project,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(WorkoutTableEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(WorkoutTableEntry.COLUMN_GYM_DATE);

            //Toast.makeText(getBaseContext(), "Query count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();

            // Only set TextViews if something is found
            if (cursor.moveToNext() && cursor.getCount() > 0) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                currentDate = cursor.getString(dateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView

                TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
                dateTextView.setText(currentDate);

                TextView exerciseTextView = (TextView) findViewById(R.id.exercise_text_view);
                exerciseTextView.setText("Exercises: ");

                //query exercise DB to populate this TextView
                String[] project2 = {
                        ExerciseTableEntry._ID,
                        ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT,
                        ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE
                };

                String whereClause2 = ExerciseTableEntry.COLUMN_WORKOUT_PARENT + "=?";

                final Cursor cursor2 = db2.query(ExerciseTableEntry.TABLE_NAME,
                        project2,                               // The columns to return
                        whereClause2,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                int idColumnIndex2 = cursor2.getColumnIndex(ExerciseTableEntry._ID);
                int exerciseColumnIndex = cursor2.getColumnIndex(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE);

                // iterate through all associated exercises, appending them to textview
                while(cursor2.moveToNext()) {
                    currentInnerID = cursor2.getInt(idColumnIndex2);
                    exerciseTextView.append("\n" + cursor2.getString(exerciseColumnIndex));
                }

                cursor2.close();

                // Only setup FAB if we found something - FAB to edit workout/exercises
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getBaseContext(), "Gym edit: " + currentDate, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(WorkoutViewAdvActivity.this, WorkoutEditActivity.class);
                        i.putExtra(WorkoutTableEntry._ID, currentID);
                        i.putExtra(WorkoutTableEntry.COLUMN_GYM_DATE, currentDate);
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
