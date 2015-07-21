package com.droolingsheep.commutetimer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asbransc on 7/4/15.
 */
public class CommuteRecordDbHelper extends SQLiteOpenHelper {

    //DB Schema: Each row is one "commute" e.g. from home to work
    //Columns: Primary key
    //         8 datetimes: SQLite doesn't have a datetime type so use unix time as integer
    //                      leave home,
    //                      arrive at stop,
    //                      board bus,
    //                      alight from bus,
    //                      arrive at transfer stop,
    //                      board transfer bus,
    //                      alight from transfer bus,
    //                      arrive at work
    //
    //         String: Route 1 name
    //         String: Route 2 name
    //         String: Route 1 boarding location  -- possibly use the muni stop id instead? probably requires lookup
    //         String: Route 1 alighting location
    //         String: Route 2 boarding location
    //         String: Route 2 alighting location
    //         Integer: 0 = to work, 1 = to home, 2 = other.
    //         Integer: bus 1 number
    //         Integer: bus 2 number
    // Any others? This is a lot of columns. Does it make more sense to make each row one bus ride?
    private static final int VERSION = 1;
    private static final String NAME = "commute_record_db";
    public static final String TABLE_NAME = "commute_records";

    public static final String KEY_ID = "_id";

    public static final String KEY_LEAVE_TIME = "leave_time";
    public static final String KEY_AT_STOP_1_TIME = "at_stop_1_time";
    public static final String KEY_BOARD_1_TIME = "board_1_time";
    public static final String KEY_ALIGHT_1_TIME = "alight_1_time";
    public static final String KEY_AT_STOP_2_TIME = "at_stop_2_time";
    public static final String KEY_BOARD_2_TIME = "board_2_time";
    public static final String KEY_ALIGHT_2_TIME = "alight_2_time";
    public static final String KEY_ARRIVE_TIME = "arrive_time";

    public static final String KEY_ROUTE_1_NAME = "route_1_name";
    public static final String KEY_ROUTE_2_NAME = "route_2_name";
    public static final String KEY_ROUTE_1_BOARD_LOCATION = "route_1_board_location";
    public static final String KEY_ROUTE_1_ALIGHT_LOCATION = "route_1_alight_location";
    public static final String KEY_ROUTE_2_BOARD_LOCATION = "route_2_board_location";
    public static final String KEY_ROUTE_2_ALIGHT_LOCATION = "route_2_alight_location";

    public static final String KEY_DIRECTION = "direction";
    public static final String KEY_BUS_1_NUMBER = "bus_1_number";
    public static final String KEY_BUS_2_NUMBER = "bus_2_number";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " + //alias for rowid
            KEY_LEAVE_TIME + " INTEGER, " +
            KEY_AT_STOP_1_TIME + " INTEGER, " +
            KEY_BOARD_1_TIME + " INTEGER, " +
            KEY_ALIGHT_1_TIME + " INTEGER, " +
            KEY_AT_STOP_2_TIME + " INTEGER, " +
            KEY_BOARD_2_TIME + " INTEGER, " +
            KEY_ALIGHT_2_TIME + " INTEGER, " +
            KEY_ARRIVE_TIME + " INTEGER, " +
            KEY_ROUTE_1_NAME + " TEXT, " +
            KEY_ROUTE_2_NAME + " TEXT, " +
            KEY_ROUTE_1_BOARD_LOCATION + " TEXT, " +
            KEY_ROUTE_1_ALIGHT_LOCATION + " TEXT, " +
            KEY_ROUTE_2_BOARD_LOCATION + " TEXT, " +
            KEY_ROUTE_2_ALIGHT_LOCATION + " TEXT, " +
            KEY_DIRECTION + " INTEGER, " +
            KEY_BUS_1_NUMBER + " INTEGER, " +
            KEY_BUS_2_NUMBER + " INTEGER);";

    public CommuteRecordDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Do nothing since there's only one version right now
    }

    public Cursor getAllHistory() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        //return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
}
