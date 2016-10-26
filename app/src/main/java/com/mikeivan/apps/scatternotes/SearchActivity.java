package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This activity is used to pick which database to search.
 * Each database have it's own search activity.
 * (workout and exercise are put together)
 */
public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Setup the TextViews
        TextView search_memo = (TextView) findViewById(R.id.do_search_memo);
        TextView search_beer = (TextView) findViewById(R.id.do_search_beer);
        TextView search_exercise = (TextView) findViewById(R.id.do_search_exercise);
        TextView search_movie = (TextView) findViewById(R.id.do_search_movie);

        // Set a click listener on search memo View
        search_memo.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent resultsIntent = new Intent(SearchActivity.this, MemoSearchActivity.class);
                startActivity(resultsIntent);
                finish();
           }
        });

        // Set a click listener on search beer View
        search_beer.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent resultsIntent = new Intent(SearchActivity.this, BeerSearchActivity.class);
                startActivity(resultsIntent);
                finish();
            }
        });

        // Set a click listener on search exercise View
        search_exercise.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent resultsIntent = new Intent(SearchActivity.this, ExerciseSearchActivity.class);
                startActivity(resultsIntent);
                finish();
            }
        });

        // Set a click listener on search movie View
        search_movie.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent resultsIntent = new Intent(SearchActivity.this, MovieSearchActivity.class);
                startActivity(resultsIntent);
                finish();
            }
        });

    }
}
