package com.pasqualiselle.timemanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasqualiselle.timemanager.data.TimeManagerContract;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

       /* LinearLayout ll = findViewById(R.id.scrollHistoricLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 120; i++) {
            TextView aHistoricLineTextView = new TextView(this);
            aHistoricLineTextView.setText( "test"+":"+i );
            ll.addView(aHistoricLineTextView, lp);
        }*/


    }
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the ACTIVITIES database.
     */

    private void displayDatabaseInfo(){

        //projection is just a name for the Columns that we were interested in getting back
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {

                TimeManagerContract.ActivityEntry._ID,
                TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME

        };

        Cursor cursor = getContentResolver().query(
                TimeManagerContract.ActivityEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

    }

}
