package com.alineselle.timemananger;

import android.icu.text.TimeZoneFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.text.DateFormat.DAY_OF_WEEK_FIELD;
import static java.text.DateFormat.WEEK_OF_MONTH_FIELD;
import static java.text.DateFormat.getTimeInstance;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar calendar = Calendar.getInstance();
       String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.textView_day_of_the_week);
        textViewDate.setText(String.valueOf(currentDate));

        Thread t = new Thread(){
            @Override
            public void run() {
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                TextView tdate= findViewById(R.id.text_view_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }


                        });
                    }
                } catch(InterruptedException e){
                }
            }
        };
        t.start();


    }
}
