package com.pasqualiselle.timemanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.pasqualiselle.timemanager.DetailsActivity;
import com.pasqualiselle.timemanager.R;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

import java.util.concurrent.TimeUnit;

/**
 * {@link HistoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class HistoryCursorAdapter extends CursorAdapter {


    /**
     * @param context     The context
     * @param c           The cursor from which to get the data
     * @param autoRequery I dont know what is this parameter, it needs study
     */
    public HistoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
    }


    /**
     * This method binds the activity data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current activity can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView activityNameTxtView = view.findViewById(R.id.activityNameTextView);
        TextView durationTxtView = view.findViewById(R.id.durationTextView);

        //Extract properties from the cursor
        final String activityName = cursor.getString(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));
        final int activityId = cursor.getInt(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry._ID));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivitiesDuration.COLUMN_DURATION));


        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

        //populated fields with extracted properties
        activityNameTxtView.setText(activityName);
        durationTxtView.setText(hms);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DetailsActivity.class);
                intent.putExtra("activity_name" , activityName);
                intent.putExtra("activity_id", activityId);

                context.startActivity(intent);

            }
        });
    }


}
