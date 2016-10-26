package com.mikeivan.apps.scatternotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mivan on 9/7/2016.
 *
 * This class extends ArrayAdapter to be used with BeerEntry.
 * This enables use of ScrollView to display database data
 */
public class BeerAdapter extends ArrayAdapter<BeerEntry> {

    // Constructor, which calls super, passing in 0 for default resource file,
    // which is defined later in getView
    public BeerAdapter(Activity context, ArrayList<BeerEntry> beers) {
        super(context, 0, beers);
    }

    // Override getView to display according to beer_list_item.xml
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.beer_list_item, parent, false);
        }

        // Get the BeerEntry object located at this position in the list
        BeerEntry currentBeer = getItem(position);

        // Find the TextView in the beer_list_item.xml layout with the company name
        // and set it to the beer company
        TextView companyTextView = (TextView) listItemView.findViewById(R.id.company_text_view);
        companyTextView.setText(currentBeer.getCompany());

        // Find the TextView in the beer_list_item.xml layout with the product name
        // and set it to the beer product
        TextView productTextView = (TextView) listItemView.findViewById(R.id.product_text_view);
        productTextView.setText(currentBeer.getProduct());

        // Find the TextView in the beer_list_item.xml layout with the date
        // and set it to the date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentBeer.getDate());

        return listItemView;
    }

}
