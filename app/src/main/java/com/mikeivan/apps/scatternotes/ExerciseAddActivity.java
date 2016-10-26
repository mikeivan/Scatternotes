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

import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

/*
 * This activity adds an activity into the database.
 */
public class ExerciseAddActivity extends AppCompatActivity {

    // parent_id to associate with exercise, passed in via intent
    int parent_id;

    //EditText field to enter exercise
    private EditText mExerciseEditText;

    /*
     * This function adds a new exercise into the exercise database
     * date used is the date of creation of the parent in workout database
     */
    private boolean insertExercise() {
        // Retrieve information in EditText
        String exerciseString = mExerciseEditText.getText().toString().trim();

        if(exerciseString.equals("")) {
            Toast.makeText(getBaseContext(), "Exercise cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Create database interaction objects
        ExerciseDbHelper mDbHelper = new ExerciseDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_PARENT, parent_id);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, exerciseString);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_DATE, getIntent().getStringExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ExerciseTableEntry.TABLE_NAME, null, values);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Exercise saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with saving exercise", Toast.LENGTH_SHORT).show();
            Log.v("ExerciseAddActivity", "Error with saving exercise");
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_add);

        parent_id = getIntent().getIntExtra(ExerciseTableEntry.COLUMN_WORKOUT_PARENT, -1);

        //Toast.makeText(getBaseContext(), "parent_id: " + parent_id, Toast.LENGTH_SHORT).show();

        // Setup FAB to save exercise into database
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(insertExercise()) {
                    Intent i = new Intent(ExerciseAddActivity.this, WorkoutViewAdvActivity.class);
                    i.putExtra("GymPosition", parent_id);
                    startActivity(i);
                    finish();
                }
            }
        });

        // Setup EditText
        mExerciseEditText = (EditText) findViewById(R.id.edit_exercise);
    }
}
