package com.pasqualiselle.timemanager.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY,TimeManagerContract.PATH_ACTIVITIES+"/#",ACTIVITY_ID);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY,TimeManagerContract.PATH_INSTANCES+"/#",INSTANCE_ID);

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

    //I did a modificiation here, changed some parameters projection,selection,selectionArgs
    @Nullable
    @Override
    public Cursor query( @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,  @Nullable String[] selectionArgs,
                         @Nullable String sortOrder) {
        //Get readable database
        SQLiteDatabase database = mTimeManagerDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch(match){

            case ACTIVITIES:
                //For the ACTIVITIES code, query the ACTIVITIES table directly with the give
                //projection,selection,selection arguments,and sort order.The cursor
                //could contain multiple rows of the pets table
                // TODO: Perform database query on ACTIVITIES table

                cursor = database.query(TimeManagerContract.ActivityEntry.TABLE_NAME, projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case INSTANCES:
                cursor = database.query(TimeManagerContract.InstanceEntry.TABLE_NAME, projection,selection,selectionArgs,
                       null,null,sortOrder);
                break;

            case ACTIVITY_ID:
                // For the ACTIVITY_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.activities/activities/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = TimeManagerContract.ActivityEntry._ID + "=?";
                selectionArgs =new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the ACTIVITIES table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TimeManagerContract.ActivityEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+ uri);

        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match){

            case ACTIVITIES:
                return insertActivity(uri,contentValues);
            case INSTANCES:
                return insertInstance(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }

    }

    //creating a new method for inserting activity. We are going to use insertActivity() in the Uri insert above
    /**
     * Insert an activity into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */

    private Uri insertActivity(Uri uri, ContentValues values){
        // TODO: Insert a new activity into the ACTIVITIES database table with the given ContentValues
        //Get writable database
        SQLiteDatabase database = mTimeManagerDbHelper.getWritableDatabase();

        //Insert the new activity with the given values.Once we have a database object, we can call the insert() method on it,
        // passing in the ACTIVITY table name and the ContentValues object. The return value is the ID of the new row that was just created,
        // in the form of a long data type (which can store numbers larger than the int data type).

        long id = database.insert(TimeManagerContract.ActivityEntry.TABLE_NAME, null, values);

        //If the id is -1, then the insertion failed. Logan error and return null
        if(id == -1){

            Log.e(LOG_TAG, "Failed to insert row for "+uri);
            return null;
        }

        //Once we know the ID of the new row in the table,
        //return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri,id);

    }

    private Uri insertInstance(Uri uri, ContentValues values){

        // TODO: Insert a new instance into the intance database table with the given ContentValues
        //Get writable database
        SQLiteDatabase database = mTimeManagerDbHelper.getWritableDatabase();

        //Insert the new instance with the given values.Once we have a database object, we can call the insert() method on it,
        // passing in the INSTANCES table name and the ContentValues object. The return value is the ID of the new row that was just created,
        // in the form of a long data type (which can store numbers larger than the int data type).
       long id = database.insert(TimeManagerContract.InstanceEntry.TABLE_NAME, null, values);

       //If the id is -1, then the insertion failed. Logan error and return null
        if(id == -1){

            Log.e(LOG_TAG,"Failed to insert row for "+uri);
            return null;
        }

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri,id);

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
