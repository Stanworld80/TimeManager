package com.pasqualiselle.timemanager.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pasqualiselle.timemanager.R;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailsCursorAdapter extends CursorAdapter {

    /**
     * @param context     The context
     * @param c           The cursor from which to get the data
     * @param autoRequery I dont know what is this parameter, it needs study
     */
    public DetailsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
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
        return LayoutInflater.from(context).inflate(R.layout.detail_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView instanceDateView = view.findViewById(R.id.instanceDateView);
        TextView instanceDurationView = view.findViewById(R.id.instanceDurationView);

        long startTime  = cursor.getLong(cursor.getColumnIndexOrThrow(TimeManagerContract.InstanceEntry.COLUMN_START_TIME));
        long endTime  = cursor.getLong(cursor.getColumnIndexOrThrow(TimeManagerContract.InstanceEntry.COLUMN_END_TIME));
        long duration = endTime - startTime;

        Date startTimeDate = new Date(startTime);
        SimpleDateFormat dtF = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        String startTimeTxt = dtF.format(startTimeDate);

        String durationTxt = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

        instanceDateView.setText(startTimeTxt);

        instanceDurationView.setText(durationTxt);

    }

}
