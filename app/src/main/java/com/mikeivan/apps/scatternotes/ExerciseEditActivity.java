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
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

/*
 * This activity is used to update or delete an exercise row.
 */
public class ExerciseEditActivity extends AppCompatActivity {

    //EditText field to enter exercise
    private EditText mExerciseEditText;

    /*
     * This function updates an exercise in the exercise database
     * date is unaltered - still date of creation
     */
    private boolean updateExercise() {
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
        values.put(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, exerciseString);

        // Create the where clause, so we update only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = ExerciseContract.ExerciseTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(ExerciseContract.ExerciseTableEntry._ID, -1) };

        // Update the row, returning the number of rows updated (should be 1 or 0)
        long newRowId = db.update(ExerciseContract.ExerciseTableEntry.TABLE_NAME, values, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Exercise saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with saving exercise", Toast.LENGTH_SHORT).show();
            Log.v("ExerciseEditActivity", "Error with updating exercise");
            return false;
        }
    }

    /*
     * This function deletes an exercise row from the exercise database
     */
    private void deleteExercise() {
        // Create database interaction objects
        ExerciseDbHelper mDbHelper = new ExerciseDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create the where clause, so we delete only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = ExerciseContract.ExerciseTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(ExerciseContract.ExerciseTableEntry._ID, -1) };

        // delete the row, returning the number of rows deleted (should be 1 or 0)
        long newRowId = db.delete(ExerciseContract.ExerciseTableEntry.TABLE_NAME, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Exercise deleted: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with deleting exercise", Toast.LENGTH_SHORT).show();
            Log.v("ExerciseEditActivity", "Error with deleting exercise");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_edit);

        // Setup EditText
        mExerciseEditText = (EditText) findViewById(R.id.edit_exercise);

        // Setup FAB to update exercise
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateExercise()) {
                    Intent i = new Intent(ExerciseEditActivity.this, WorkoutViewActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        // Setup FAB to delete exercise
        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExercise();
                Intent i = new Intent(ExerciseEditActivity.this, WorkoutViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Populate EditText with actual value from the db, this should make editing easier
        mExerciseEditText.setText(getIntent().getStringExtra(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE));

    }
}
