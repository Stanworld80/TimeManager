package com.pasqualiselle.timemanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.pasqualiselle.timemanager.data.TimeManagerContract;
import com.pasqualiselle.timemanager.adapters.HistoryCursorAdapter;

public class HistoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TIMEMANAGER", this.getClass()+" : onCreate called.");

        setContentView(R.layout.activity_history);
        setTitle("History");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TIMEMANAGER", this.getClass()+" : onResume called.");
        displayDatabaseInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TIMEMANAGER", this.getClass()+" : onRestart called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TIMEMANAGER ", this.getClass()+" : onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TIMEMANAGER", this.getClass()+" : onStop called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TIMEMANAGER", this.getClass()+" : onDestroy called.");
    }


    /**
         * Temporary helper method to display information in the onscreen TextView about the state of
         * the ACTIVITIES database.
         */
    private void displayDatabaseInfo() {

        Cursor cursor = getContentResolver().query(
                TimeManagerContract.ActivitiesDuration.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Find ListView to populate with the activity name data
        ListView timeManagerListItemsView = findViewById(R.id.listViewItems);

        //Setup cursor adapter using cursor from_last step
        HistoryCursorAdapter timeManagerCursorAdapter = new HistoryCursorAdapter(this,cursor,false);

        //Attach cursor adapter to the Listview
        timeManagerListItemsView.setAdapter(timeManagerCursorAdapter);


    }

}
