package com.pasqualiselle.timemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.text.DateFormat.getTimeInstance;

public class MainActivity extends AppCompatActivity {


    public static final int CURRENT_ACTIVITY_REQUEST_CODE = 42;

    EditText mEditActivity;

    private SharedPreferences mPreferences;
    public static final String PREF_KEY_ACTIVITY_NAMES = "PREF_KEY_ACTIVITY_NAMES";
    public static final String PREF_KEY_ACTIVITY_NAME_NUMBER1 = "PREF_KEY_ACTIVITY_NAME_NUMBER1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCurrentDateAndTime();
        goToCurrentActivity();

        Button historyBtn = findViewById(R.id.history_btn);
        historyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }








    public void goToCurrentActivity() {
        final Button mStartBtn;
        mStartBtn = findViewById(R.id.start_btn);

        EditText mEditActivity;
        mEditActivity = findViewById(R.id.editTextAcitivity);

        mEditActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStartBtn.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

                mStartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mEditActivity;

                        mEditActivity = findViewById(R.id.editTextAcitivity);
                        //to Save the input activity name and store it in preferences
                        String activity_Name = mEditActivity.getText().toString();
                        mPreferences = getSharedPreferences(PREF_KEY_ACTIVITY_NAMES,MODE_PRIVATE);
                        mPreferences.edit().putString(PREF_KEY_ACTIVITY_NAME_NUMBER1,activity_Name).apply();
                        Intent intent = new Intent(MainActivity.this, CurrentActivity.class);
                        startActivityForResult(intent,CURRENT_ACTIVITY_REQUEST_CODE);
                    }
                });

            }
        });

    }


    public void setCurrentDateAndTime() {

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.textView_day_of_the_week);
        textViewDate.setText(currentDate);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = findViewById(R.id.text_view_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }


                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

    }
}

