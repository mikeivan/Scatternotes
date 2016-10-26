package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.BeerDbHelper;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;

import java.util.ArrayList;

/*
 * This activity displays the beers in the database
 */
public class BeerViewActivity extends AppCompatActivity {

    // Gives us access to beer db
    private BeerDbHelper mBeerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        mBeerDbHelper = new BeerDbHelper(this);
        ArrayList<BeerEntry> beers = new ArrayList<BeerEntry>();

        SQLiteDatabase db = mBeerDbHelper.getReadableDatabase();

        String[] project = {
                BeerTableEntry._ID,
                BeerTableEntry.COLUMN_BEER_COMPANY,
                BeerTableEntry.COLUMN_BEER_PRODUCT,
                BeerTableEntry.COLUMN_BEER_DATE
        };

        Cursor cursor = db.query(BeerTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
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
            int dateColumnIndex = cursor.getColumnIndex(BeerTableEntry.COLUMN_BEER_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentCompany = cursor.getString(companyColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Create a new BeerEntry for each row
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

                Intent i = new Intent(BeerViewActivity.this, BeerViewAdvActivity.class);
                i.putExtra("BeerPosition", currentBeer.getId());
                startActivity(i);
                finish();
            }
        });

    }

}
