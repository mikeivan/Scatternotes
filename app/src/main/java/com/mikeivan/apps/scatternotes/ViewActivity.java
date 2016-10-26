package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/*
 * This activity is for picking which database to view.
 */
public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Setup TextViews
        TextView memo_cat = (TextView) findViewById(R.id.memo_category);
        TextView beer_cat = (TextView) findViewById(R.id.beer_category);
        TextView gym_cat = (TextView) findViewById(R.id.gym_category);
        TextView movie_cat = (TextView) findViewById(R.id.movie_category);

        // Set a click listener on memo view
        memo_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewActivity.this, MemoViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Set a click listener on beer view
        beer_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewActivity.this, BeerViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Set a click listener on workout view
        gym_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewActivity.this, WorkoutViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Set a click listener on movie view
        movie_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewActivity.this, MovieViewActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
