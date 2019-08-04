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
    // as a global constant variable, so all log messages from the TimemanagerProvider will have
    // the same log tag identifier when you are reading the system logs.
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = TimeManagerProvider.class.getSimpleName();

    //Database Helper Object
    private TimeManagerDbHelper mTimeManagerDbHelper;


    /**
     * URI matcher code for the content URI for the tables
     */
    private static final int URIMATCHCODE_ACTIVITIES = 100;
    private static final int URIMATCHCODE_INSTANCES = 200;
    private static final int URIMATCHCODE_ACTIVITIES_DURATIONS = 100200;


    /**
     * URI matcher code for the content URI for a single ACTIVITY in the ACTIVITIES table
     * URI matcher code for the content URI for a single INSTANCE in the INSTANCES table
     */
    private static final int URIMATCHCODE_ACTIVITY_ID = 101;
    private static final int URIMATCHCODE_INSTANCE_ID = 201;



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
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_ACTIVITIES, URIMATCHCODE_ACTIVITIES);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_INSTANCES, URIMATCHCODE_INSTANCES);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_ACTIVITIES + "/#", URIMATCHCODE_ACTIVITY_ID);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_INSTANCES + "/#", URIMATCHCODE_INSTANCE_ID);
        sUriMatcher.addURI(TimeManagerContract.CONTENT_AUTHORITY, TimeManagerContract.PATH_ACTIVITIES_DURATION, URIMATCHCODE_ACTIVITIES_DURATIONS);
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

    //I did a modification here, changed some parameters projection,selection,selectionArgs
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        //Get readable database
        SQLiteDatabase database = mTimeManagerDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match) {

            case URIMATCHCODE_ACTIVITIES:
                //For the ACTIVITIES code, query the ACTIVITIES table directly with the give
                //projection,selection,selection arguments,and sort order.The cursor
                //could contain multiple rows of the pets table

                cursor = database.query(TimeManagerContract.ActivityEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case URIMATCHCODE_INSTANCES:
                cursor = database.query(TimeManagerContract.InstanceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case URIMATCHCODE_ACTIVITY_ID:
                // For the ACTIVITY_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.activities/activities/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = TimeManagerContract.ActivityEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the ACTIVITIES table where the _id equals 3 to return a
                // Cursor containing that row of the table
                cursor = database.query(TimeManagerContract.ActivityEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case URIMATCHCODE_ACTIVITIES_DURATIONS:
                String q = ("SELECT " + TimeManagerContract.ActivityEntry.TABLE_NAME + "." +
                        TimeManagerContract.ActivityEntry._ID +
                        " , " + TimeManagerContract.InstanceEntry.COLUMN_START_TIME +
                        " , " + TimeManagerContract.InstanceEntry.COLUMN_END_TIME +
                        " , " + TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME +
                        " , SUM(" +
                        TimeManagerContract.InstanceEntry.COLUMN_END_TIME +
                        "-" + TimeManagerContract.InstanceEntry.COLUMN_START_TIME +
                        ")" +
                        " AS " + TimeManagerContract.ActivitiesDuration.COLUMN_DURATION +
                        " FROM " +
                        TimeManagerContract.InstanceEntry.TABLE_NAME
                        + "  , " +
                        TimeManagerContract.ActivityEntry.TABLE_NAME +
                        " WHERE " +
                        TimeManagerContract.ActivityEntry.TABLE_NAME + "." +
                        TimeManagerContract.ActivityEntry._ID + " = " +
                        TimeManagerContract.InstanceEntry.TABLE_NAME + "." +
                        TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID +
                        " GROUP BY " +
                        TimeManagerContract.InstanceEntry.TABLE_NAME + "." +
                        TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID +
                        " ORDER BY "+ TimeManagerContract.InstanceEntry.COLUMN_END_TIME +" DESC");

                Log.d("DURATIONS_QUERIES", "query: " + q);
                cursor = database.rawQuery(q, null);

                //for debug purposes,look at LogCat
                while (cursor.moveToNext()) {
                    String activityName = cursor.getString(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));
                    long start = cursor.getLong(cursor.getColumnIndexOrThrow(TimeManagerContract.InstanceEntry.COLUMN_START_TIME));
                    long end = cursor.getLong(cursor.getColumnIndexOrThrow(TimeManagerContract.InstanceEntry.COLUMN_END_TIME));
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivitiesDuration.COLUMN_DURATION));
                    Log.d("DURATIONS_QUERIES",
                            "name: " + activityName +
                                    "\tstart:" + start +
                                    "\tend:" + end +
                                    "\tdurationcount:" + duration);

                }
                cursor.moveToFirst();

                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {

            case URIMATCHCODE_ACTIVITIES:
                return getOrInsertActivity(uri, contentValues);
            case URIMATCHCODE_INSTANCES:
                return insertInstance(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }


    /***
     * Check if activity (with a specific name passed in values)
     * exists already in the database ,
     * - if the activity exists : then get it and  the returned Uri will be with
     * the _ID of the existing activity found.
     * - if the activity doesn't exist : then insert it and the returned Uri
     * will be with the new _ID of the inserted activity.
     * the
     * @param uri
     * @param values
     * @return Return a uri to access the activity found or inserted
     */
    private Uri getOrInsertActivity(Uri uri, ContentValues values) {
        Uri result;
        SQLiteDatabase database = mTimeManagerDbHelper.getReadableDatabase();

        Log.d(LOG_TAG, "getOrInsertActivity: trying with :" + uri + " and name:" + values.getAsString(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));

        String[] projection = {
                TimeManagerContract.ActivityEntry._ID,
                TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME
        };

        String selection = TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME + " LIKE ?";

        String[] selectionArgs = new String[]{
                values.getAsString(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME)
        };


        Cursor cursor = database.query(TimeManagerContract.ActivityEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int nbActivityWithThatName = cursor.getCount();
        if (nbActivityWithThatName == 1) {

            /* // FOR DEBUGGING PURPOSE
            Log.d(LOG_TAG, "getOrInsertActivity: nbColumn : "+cursor.getColumnCount());
            for (String n : cursor.getColumnNames()) {
                Log.d(LOG_TAG, "getOrInsertActivity: name : " + n + " : "+
                        cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry._ID));
            }*/

            int nb = cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry._ID);

            Log.d(LOG_TAG, "getOrInsertActivity: trying to get the ID , on column " + nb);
            cursor.moveToFirst(); // THIS IS VERY IMPORTANT !!!!
            int theId = cursor.getInt(nb);
            result = ContentUris.withAppendedId(uri, theId);
            Log.d(LOG_TAG, "getOrInsertActivity: " + theId);
            return result;
        } else if (nbActivityWithThatName == 0) {
            Log.d(LOG_TAG, "getOrInsertActivity: not found, so inserting");

            result = insertActivity(uri, values);

            return result;
        } else {
            /*
            TODO : should throw a error exception here ,
              because there should be only 1 or 0 activity
              with a name in the base it is very strange .
             */
            return null;
        }
    }
    //creating a new method for inserting activity. We are going to use insertActivity() in the Uri insert above

    /**
     * Insert an activity into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */

    private Uri insertActivity(Uri uri, ContentValues values) {
        //Get writable database
        SQLiteDatabase database = mTimeManagerDbHelper.getWritableDatabase();

        //Insert the new activity with the given values.Once we have a database object, we can call the insert() method on it,
        // passing in the ACTIVITY table name and the ContentValues object. The return value is the ID of the new row that was just created,
        // in the form of a long data type (which can store numbers larger than the int data type).

        long id = database.insert(TimeManagerContract.ActivityEntry.TABLE_NAME, null, values);

        //If the id is -1, then the insertion failed. Log an error and return null
        if (id == -1) {

            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Once we know the ID of the new row in the table,
        //return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    private Uri insertInstance(Uri uri, ContentValues values) {
        //Get writable database
        SQLiteDatabase database = mTimeManagerDbHelper.getWritableDatabase();

        //Insert the new instance with the given values.Once we have a database object, we can call the insert() method on it,
        // passing in the INSTANCES table name and the ContentValues object. The return value is the ID of the new row that was just created,
        // in the form of a long data type (which can store numbers larger than the int data type).
        long id = database.insert(TimeManagerContract.InstanceEntry.TABLE_NAME, null, values);

        //If the id is -1, then the insertion failed. Logan error and return null
        if (id == -1) {

            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        } else
            Log.d(LOG_TAG, "insertInstance: id=  " + id);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    /**
     *
     * @param uri the URI to acess Data to delete
     * @param selection would be an additional filter to the whereClause  (ignored for now)
     * @param selectionArgs would be the args for the filter additional (ignored for now)
     * @return number of deleted rows.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Get readable database
        SQLiteDatabase database = mTimeManagerDbHelper.getWritableDatabase();

        //This cursor will hold the result of the query
        int result = 0;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);



        switch (match) {
            case URIMATCHCODE_ACTIVITY_ID:
                String whereClause = TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID + "=?";
                String[] whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result =  database.delete(TimeManagerContract.InstanceEntry.TABLE_NAME, whereClause, whereArgs);
                whereClause = TimeManagerContract.ActivityEntry._ID + "=?";
                result +=  database.delete(TimeManagerContract.ActivityEntry.TABLE_NAME, whereClause, whereArgs);
            break;
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
