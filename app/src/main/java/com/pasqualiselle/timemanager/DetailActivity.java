package com.pasqualiselle.timemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {

    Button mClearDataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mClearDataBtn = findViewById(R.id.clearButton);
        final String activityName = "activityName";

        mClearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( DetailActivity.this, R.style.AlertDialogStyle );
                builder.setTitle( "DELETE DATA" )
                        .setMessage("Are you sure you want to delete" + activityName + " activity data?")
                        .setPositiveButton( "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



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
}
