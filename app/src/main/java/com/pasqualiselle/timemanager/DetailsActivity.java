package com.pasqualiselle.timemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pasqualiselle.timemanager.adapters.DetailsCursorAdapter;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

import java.net.URI;

public class DetailsActivity extends AppCompatActivity {

    Button mClearDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d("TIMEMANAGER ", this.getClass()+" : OnCreate called.");

        setTitle("Detail");
        // getting datas
        Intent localIntent = getIntent();
        final String activityName = localIntent.getStringExtra("activity_name");
        final int activityId = localIntent.getIntExtra("activity_id", 0);

        // settings views
        TextView titleView = findViewById(R.id.activityTitle);
        titleView.setText(activityName);
        displayInstancesList(activityId);

        //TODO : here should go a alertdialog on the button to clear data
        mClearDataBtn = findViewById(R.id.clearButton);

        mClearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( DetailsActivity.this, R.style.AlertDialogStyle );
                builder.setTitle( "DELETE DATA" )
                        .setMessage("Are you sure you want to delete " + "'"+activityName+"'" +" data?")
                        .setPositiveButton( "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri dataToDelete = Uri.withAppendedPath(TimeManagerContract.ActivityEntry.CONTENT_URI, "/"+activityId);

                                getContentResolver().delete(dataToDelete,null,null);

                              //  setResult(RESULT_OK,null);
                                finish();

                            }

                        } )
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        } )
                        //what is .setCancelable for?
                        .setCancelable(false)
                        .create()
                        .show();
            }


        });
    }


    public void displayInstancesList(int activityId)
    {
        String[] projection =
                {
                        TimeManagerContract.InstanceEntry._ID,
                        TimeManagerContract.InstanceEntry.COLUMN_START_TIME,
                        TimeManagerContract.InstanceEntry.COLUMN_END_TIME
                };
        String whereSelection = TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID + "=?";
        String[] whereParams = { String.valueOf(activityId)};
        String sortOrder  =  TimeManagerContract.InstanceEntry.COLUMN_END_TIME + " DESC";

        Cursor cursor = getContentResolver().query(
                TimeManagerContract.InstanceEntry.CONTENT_URI,
                projection,
                whereSelection,
                whereParams,
                sortOrder);

        //Find ListView to populate with the activity name data
        ListView detailsListItemsView = findViewById(R.id.listViewItems);

        //Setup cursor adapter using cursor from_last step
        DetailsCursorAdapter theDetailsCursorAdapter = new DetailsCursorAdapter(this,cursor,false);

        //Attach cursor adapter to the Listview
        detailsListItemsView.setAdapter(theDetailsCursorAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TIMEMANAGER ", this.getClass()+" : onResume called.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TIMEMANAGER ", this.getClass()+" : onRestart called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TIMEMANAGER ", this.getClass()+" : onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TIMEMANAGER ", this.getClass()+" : onStop called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TIMEMANAGER ", this.getClass()+" : onDestroy called.");
    }
}
