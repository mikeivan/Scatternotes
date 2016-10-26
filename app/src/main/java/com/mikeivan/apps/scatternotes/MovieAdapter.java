package com.mikeivan.apps.scatternotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mivan on 9/19/2016.
 *
 * This class extends ArrayAdapter to be used with MovieEntry.
 * This enables use of ScrollView to display database data
 */
public class MovieAdapter extends ArrayAdapter<MovieEntry> {

    // Constructor, which calls super, passing in 0 for default resource file,
    // which is defined later in getView
    public MovieAdapter(Activity context, ArrayList<MovieEntry> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        // Get the MovieEntry object located at this position in the list
        MovieEntry currentMovie = getItem(position);

        // Find the TextView in the movie_list_item.xml layout with the movie title
        // and set it to the movie title
        TextView movieTextView = (TextView) listItemView.findViewById(R.id.movie_text_view);
        movieTextView.setText(currentMovie.getMovie());

        // Find the TextView in the movie_list_item.xml layout with the movie date
        // and set it to the movie date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentMovie.getDate());

        return listItemView;
    }

}