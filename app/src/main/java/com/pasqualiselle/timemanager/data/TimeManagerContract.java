package com.pasqualiselle.timemanager.data;

import android.provider.BaseColumns;

public class TimeManagerContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TimeManagerContract(){}

    /**
     * Inner class that defines constant values for the time_manager database table.
     * Each entry in the table represents a single activity and its associated name.
     */

    public static final class ActivitiesEntry implements BaseColumns {

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
    public static final class InstancesEntry implements BaseColumns {

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
         * Type: TEXT
         */
        public final static String COLUMN_START_TIME="startTime";



        /**
         * Datetime of the end of the instance.
         *
         * Type: TEXT
         */
        public final static String COLUMN_END_TIME ="endTime";

    }
}
