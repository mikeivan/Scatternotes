package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;

/*
 * This activity is used to search within the exercise database.
 * Since the exercise only contains one column to search on, there's only
 * one search field.
 *
 * The search query string is sent via intent to the ExerciseSearchResultsActivity,
 * which will do the actual query.
 */
public class ExerciseSearchActivity extends AppCompatActivity {

    private EditText mExerciseEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search);

        // Setup the FAB to search the exercises
        FloatingActionButton fab_search_exercise = (FloatingActionButton) findViewById(R.id.fab_search_exercise);
        fab_search_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExerciseSearchActivity.this, ExerciseSearchResultsActivity.class);
                i.putExtra("query", ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE + " LIKE \"%" + mExerciseEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup the EditText
        mExerciseEditText = (EditText) findViewById(R.id.search_exercise);
    }
}
