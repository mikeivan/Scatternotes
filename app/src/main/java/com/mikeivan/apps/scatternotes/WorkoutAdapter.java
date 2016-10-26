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
 * This class extends ArrayAdapter to be used with WorkoutEntry.
 * This enables use of ScrollView to display database data
 */
public class WorkoutAdapter extends ArrayAdapter<WorkoutEntry> {

    // Constructor, which calls super, passing in 0 for default resource file,
    // which is defined later in getView
    public WorkoutAdapter(Activity context, ArrayList<WorkoutEntry> workouts) {
        super(context, 0, workouts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gym_list_item, parent, false);
        }

        // Get the WorkoutEntry object located at this position in the list
        WorkoutEntry currentWorkout = getItem(position);

        // Find the TextView in the gym_list_item.xml layout with the workout date
        // and set it to the workout date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentWorkout.getDate());

        return listItemView;
    }

}