package com.pasqualiselle.timemanager.data;

import android.provider.BaseColumns;

public class TimeManagerContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TimeManagerContract(){}

    /**
     * Inner class that defines constant values for the time_manager database table.
     * Each entry in the table represents a single activity and its duration.
     */

    public static final class ActivityAndTimeSpentEntry implements BaseColumns {

        /** Name of database table for activity_name */
        public final static String TABLE_NAME = "activity_and_time";

        /**
         * Unique ID number for the activity_and_time (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the activity.
         *
         * Type: TEXT
         */
        public final static String COLUMN_ACTIVITY_NAME ="activity_name";

        /**
         * Time of the Activity.
         *
         * Type: TEXT
         */

        public final static String COLUMN_ACTIVITY_TIME ="activity_time";

    }

}
