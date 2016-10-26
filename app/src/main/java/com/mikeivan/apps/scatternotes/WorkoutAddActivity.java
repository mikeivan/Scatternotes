package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.WorkoutContract;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * This activity adds a workout into the database.
 */
public class WorkoutAddActivity extends AppCompatActivity {


    // CalendarView (widget) to enter the date of the workout
    private CalendarView mCalView;

    /*
     * This function adds a workout into the workout database
     * date used is the date of the calendar widget.
     */
    private void insertWorkout() {
        // Setup the date format and retrieve it from the CalendarView
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = formatter.format(new Date(mCalView.getDate()));
        //Toast.makeText(getApplicationContext(), "Cal date: " + dateString, Toast.LENGTH_SHORT).show();

        // Create database interaction objects
        WorkoutDbHelper mDbHelper = new WorkoutDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkoutContract.WorkoutTableEntry.COLUMN_GYM_DATE, dateString);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WorkoutContract.WorkoutTableEntry.TABLE_NAME, null, values);

        // This is for debugging, can be commented out and/or changed to logging
        if (newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Workout saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getApplicationContext(), "Error with saving workout", Toast.LENGTH_SHORT).show();
            Log.v("WorkoutAddActivity", "Error with saving workout");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_add);


        // Setup FAB to save workout into database
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertWorkout();
                Intent i = new Intent(WorkoutAddActivity.this, WorkoutViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup CalendarView
        mCalView = (CalendarView) findViewById(R.id.gym_calendar);
    }


}

