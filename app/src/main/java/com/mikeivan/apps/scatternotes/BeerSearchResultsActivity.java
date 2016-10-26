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

import com.mikeivan.apps.scatternotes.data.BeerDbHelper;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;

import java.util.ArrayList;

/*
 * This activity queries the beer db and displays the results
 */
public class BeerSearchResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_search_results);


        setResults();

    }

    /*
     * This function performs the query and sets the results
     */
    private void setResults() {
        // Retrieve the query string from intent
        String target = getIntent().getStringExtra("query");

        // Create database interaction objects
        BeerDbHelper mBeerDbHelper = new BeerDbHelper(this);
        ArrayList<BeerEntry> beers = new ArrayList<BeerEntry>();

        SQLiteDatabase db = mBeerDbHelper.getReadableDatabase();

        String[] project = {
                BeerTableEntry._ID,
                BeerTableEntry.COLUMN_BEER_COMPANY,
                BeerTableEntry.COLUMN_BEER_PRODUCT,
                BeerTableEntry.COLUMN_BEER_RATING,
                BeerTableEntry.COLUMN_BEER_NOTES,
                BeerTableEntry.COLUMN_BEER_DATE
        };

        // Setup a cursor for the results of the query
        Cursor cursor = db.query(BeerTableEntry.TABLE_NAME,
                project,                               // The columns to return
                target,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BeerTableEntry._ID);
            int companyColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_COMPANY);
            int productColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_PRODUCT);
            int ratingColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_RATING);
            int notesColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentCompany = cursor.getString(companyColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                int currentRating = cursor.getInt(ratingColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                beers.add(new BeerEntry(currentID, currentCompany, currentProduct, currentDate));

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // Create a BeerAdapter for our beer array
        BeerAdapter adapter = new BeerAdapter(this, beers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Create a clicklistener so when an entry is clicked we are taken to
        // the advanced view of that item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                BeerEntry currentBeer = (BeerEntry) parent.getItemAtPosition(position);

                //Toast.makeText(getBaseContext(),"Clicked: " + position + " beer: " + currentBeer.getProduct(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(BeerSearchResultsActivity.this, BeerViewAdvActivity.class);
                i.putExtra("BeerPosition", currentBeer.getId());
                startActivity(i);
                finish();
            }
        });

    }


}
