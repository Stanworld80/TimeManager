package com.pasqualiselle.timemanager.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class TimeManagerContract {

    // To prevent instantiating the contract class.
    private TimeManagerContract(){}

    /**
     *  CONTENT_AUTHORITY for URI and ContentProvider usage
     *
     */
    public static final String CONTENT_AUTHORITY = "com.pasqualiselle.timemanager";


    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_ACTIVITIES = "activities";

    public static final String PATH_INSTANCES  = "instances";

    public static final String PATH_ACTIVITIES_DURATION = "activities_duration";


    //Notice that COLUMN_DURATION does not exist as a Column in the SQL
    //COLUMN_DURATION will be created from the different between COLUMN_START_TIME and COLUMN_END_TIME from the table INSTANCES
    public static final class ActivitiesDuration
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACTIVITIES_DURATION);
        public final static String COLUMN_DURATION = "duration";
    }

    /**
     * Inner class that defines constant values for the time_manager database table.
     * Each entry in the table represents a single activity and its associated name.
     */
    public static final class ActivityEntry implements BaseColumns {

        /** The content URI to access the activity data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACTIVITIES);

        /** Name of database table for activities */
        public final static String TABLE_NAME = "activities";

        /**
         * Unique ID number for the activity (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the activity.
         *
         * Type: TEXT
         */
        public final static String COLUMN_ACTIVITY_NAME ="name";

    }

    public static final class InstanceEntry implements BaseColumns {

        /** The content URI to access the instances data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INSTANCES);

        /** Name of database table for instances */
        public final static String TABLE_NAME = "instances";

        /**
         * Unique ID number for the activity_and_time (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Id of the activity.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_ACTIVITY_ID ="activityId";

        /**
         * Datetime of the beginning of the instance.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_START_TIME="startTime";



        /**
         * Datetime of the end of the instance.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_END_TIME ="endTime";

    }
}
