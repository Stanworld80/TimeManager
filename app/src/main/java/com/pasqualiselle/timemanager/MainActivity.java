package com.pasqualiselle.timemanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.pasqualiselle.timemanager.adapters.ActivitiesCursorAdapter;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static final int CURRENT_ACTIVITY_REQUEST_CODE = 42;

    AutoCompleteTextView mEditActivity;
    ActivitiesCursorAdapter mActivitiesCursorAdapter;
    String mLastInsertedActivityName;
    Long mLastInsertedActivityId;

    private SharedPreferences mPreferences;
    public static final String PREF_TIMEMANAGER_KEY = "TIMEMANAGER_PREFERENCES";
    public static final String PREF_KEY_CURRENT_ACTIVITY_NAME = "PREF_KEY_CURRENT_ACTIVITY_NAME";
    public static final String PREF_KEY_CURRENT_ACTIVITY_ID = "PREF_KEY_CURRENT_ACTIVITY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditActivity = findViewById(R.id.editTextActivity);
        prepareAutoCompletion();
        setDropDownSoftinput();
        setCurrentDateAndTime();
        goToCurrentActivity();
        setHistoryBtn();
    }

    public void setDropDownSoftinput()
    {
        mEditActivity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    /**
     * Setting for autocompletion
     */
    public void prepareAutoCompletion() {
        Cursor theInitCursor = getContentResolver().query(
                TimeManagerContract.ActivityEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        mActivitiesCursorAdapter = new ActivitiesCursorAdapter(this, theInitCursor, false);

        mEditActivity.setAdapter(mActivitiesCursorAdapter);
        mActivitiesCursorAdapter.setFilterQueryProvider(
                new FilterQueryProvider() {
                    @Override
                    public Cursor runQuery(CharSequence s) {
                        Cursor aCursor = null;
                        if (s != null) {
                            String needle = s.toString().trim();
                            if (needle.length() > 0) {
                                needle = needle.replace(' ', '%');
                                String selection = "name LIKE ?";
                                String[] selectionArgs = {"%" + needle + "%"};
                                aCursor = getContentResolver().query(
                                        TimeManagerContract.ActivityEntry.CONTENT_URI,
                                        null,
                                        selection,
                                        selectionArgs,
                                        null
                                );
                            }
                        }

                        return aCursor;
                    }
                }
        );

    }

    /**
     * Setting the "history button" to go on historyActivity when clicking
     */
    public void setHistoryBtn() {
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

    /**
     * Setting the "Start Button" to go on the current activity when clicking
     * Setting the EditActivityText view to enable or disable the startbutton depending if the text
     * is empty or not
     */

    public void goToCurrentActivity() {
        final Button mStartBtn;
        mStartBtn = findViewById(R.id.start_btn);

        mEditActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivitiesCursorAdapter.getFilter().filter(s); // filtering the autocompletion list
                mActivitiesCursorAdapter.notifyDataSetChanged();

                mStartBtn.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mStartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mEditActivity;

                        mEditActivity = findViewById(R.id.editTextActivity);
                        //to Save the input activity name and store it in preferences
                        mLastInsertedActivityName = mEditActivity.getText().toString();

                        insertActivity();
                        mPreferences = getSharedPreferences(PREF_TIMEMANAGER_KEY, MODE_PRIVATE);
                        mPreferences.edit().putString(PREF_KEY_CURRENT_ACTIVITY_NAME, mLastInsertedActivityName).apply();
                        mPreferences.edit().putLong(PREF_KEY_CURRENT_ACTIVITY_ID, mLastInsertedActivityId).apply();

                        Intent intent = new Intent(MainActivity.this, CurrentActivity.class);
                        startActivityForResult(intent, CURRENT_ACTIVITY_REQUEST_CODE);


                    }
                });
            }
        });

    }

    /**
     * Get user input from editor and save new activity into database.
     */
    private void insertActivity() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        mLastInsertedActivityName = mEditActivity.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and activity is the values.
        ContentValues values = new ContentValues();
        values.put(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME, mLastInsertedActivityName);

        //Insert a new activity into TimeManagerProvider, returning the content URI for the new activity

        Uri newUri;
        try {
            newUri = getContentResolver().insert(TimeManagerContract.ActivityEntry.CONTENT_URI, values);
            mLastInsertedActivityId = ContentUris.parseId(newUri);
        } catch (IllegalArgumentException e) {

            Log.e("MainActivity", "I found an exception while trying to insert values : " + e.getMessage());
            newUri = null;
        }
        //Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {

            //If the new content URI is null, then there was an error with insertion
            Toast.makeText(this, getString(R.string.editor_insert_activity_failed),
                    Toast.LENGTH_SHORT).show();
        } else {

            //Otherwise, the insertion was successful and we can display a toast
            Toast.makeText(this, getString(R.string.editor_insert_activity_successful),
                    Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * setting the Date in the textView_day_of_the_week View
     * and set a dynamic Time in the  text_view_date View
     */
    public void setCurrentDateAndTime() {
        // setting the First line of the date : exemple : "lundi 22 avril 2019"
        // using a Calendar object
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.textView_day_of_the_week);
        textViewDate.setText(currentDate);


        /* setting the second line of the date with the current Time , example : 03:44:25
         using "System.currentTimeMillis();" to get the system time in milliseconds then
         format it using  a SimpleDateFormat Object
        */
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = findViewById(R.id.text_view_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
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
