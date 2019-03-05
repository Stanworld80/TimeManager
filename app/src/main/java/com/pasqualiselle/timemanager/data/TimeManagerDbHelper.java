package com.pasqualiselle.timemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeManagerDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "time_manager.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
            * Constructs a new instance of {@link TimeManagerDbHelper}.
            *
            * @param context of the app
     */
    public TimeManagerDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TIME_MANAGER_TABLE = "CREATE TABLE " + TimeManagerContract.ActivityAndTimeSpentEntry.TABLE_NAME + " ("
        + TimeManagerContract.ActivityAndTimeSpentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TimeManagerContract.ActivityAndTimeSpentEntry.COLUMN_ACTIVITY_NAME + " TEXT NOT NULL, "
                + TimeManagerContract.ActivityAndTimeSpentEntry.COLUMN_ACTIVITY_TIME + " TEXT NOT NULL);";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_TIME_MANAGER_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// The database is still at version 1, so there's nothing to do be done here.
    }
}
