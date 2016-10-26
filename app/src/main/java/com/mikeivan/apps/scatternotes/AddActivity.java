package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 * This is one of the four main activities.
 * It has four textviews that take the user to
 * an add activity for each database.
 */
public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // find each textview by name
        TextView cat_add_memo = (TextView) findViewById(R.id.add_memo);
        TextView cat_add_beer = (TextView) findViewById(R.id.add_beer);
        TextView cat_add_gym = (TextView) findViewById(R.id.add_gym);
        TextView cat_add_movie = (TextView) findViewById(R.id.add_movie);

        // set onclicklisteners for each textview
        // that sends an intent to the corresponding add activity
        cat_add_memo.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, MemoAddActivity.class);
                startActivity(i);
                finish();
            }
        });

        cat_add_beer.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, BeerAddActivity.class);
                startActivity(i);
                finish();
            }
        });

        cat_add_gym.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, WorkoutAddActivity.class);
                startActivity(i);
                finish();
            }
        });

        cat_add_movie.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, MovieAddActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
