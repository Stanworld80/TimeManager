package com.pasqualiselle.timemanager.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TimeManagerProvider extends ContentProvider {

    //Since we’ll be logging multiple times throughout this file, it would be ideal to create a log tag
    // as a global constant variable, so all log messages from the PetProvider will have
    // the same log tag identifier when you are reading the system logs.
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = TimeManagerProvider.class.getSimpleName();

    //Database Helper Object
    private TimeManagerDbHelper mTimeManagerDbHelper;


    /**
     * URI matcher code for the content URI for the ACTIVITIES table
     */
    private static final int ACTIVITIES = 100;
    private static final int INSTANCES = 200;



    /**
     * URI matcher code for the content URI for a single ACTIVITY in the ACTIVITIES table
     * URI matcher code for the content URI for a single INSTANCE in the INSTANCES table
     *
     */
    private static final int ACTIVITY_ID = 101;
    private static final int INSTANCE_ID = 201;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_ACTIVITIES,ACTIVITIES);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_INSTANCES,INSTANCES);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY,TimeManagerContract.PATH_ACTIVITIES+"#",ACTIVITY_ID);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY,TimeManagerContract.PATH_INSTANCES+"#",INSTANCE_ID);

    }
    /**
     * {@link ContentProvider} for TimeManager app.
     */


    /**
     * Initialize the provider and the database helper object.
     */

    @Override
    public boolean onCreate() {
        mTimeManagerDbHelper = new TimeManagerDbHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query( @NonNull Uri uri, @Nullable String[] strings, @Nullable String s,  @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete( @NonNull Uri uri,  @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
