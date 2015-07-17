package com.droolingsheep.commutetimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asbransc on 3/20/15.
 */
public class CommuteRecord {

    private static CommuteRecord sInstance;

    private Map<Step, Long> mTimestamps = new HashMap<>();
    private Route mRoute, mTransferRoute = null;
    private Context mContext;
    private Direction mDirection;

    public CommuteRecord(Context context) {
        mContext = context;
    }

    public static CommuteRecord getInstance() {
        return sInstance;
    }

    public static void newInstance(Context context) {
        if (sInstance != null) {
            sInstance.writeToDb();
        }
        sInstance = new CommuteRecord(context);
    }

    private void writeToDb() {
        SQLiteOpenHelper dbHelper = new CommuteRecordDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        ContentValues values = convertToContentValues();
        database.insert(CommuteRecordDbHelper.TABLE_NAME, null, values);
        database.endTransaction();
        database.close();
    }

    private ContentValues convertToContentValues() {
        ContentValues values = new ContentValues();
        values.put(CommuteRecordDbHelper.KEY_LEAVE_TIME, mTimestamps.get(Step.LEAVE));
        values.put(CommuteRecordDbHelper.KEY_AT_STOP_1_TIME, mTimestamps.get(Step.AT_STOP_1));
        values.put(CommuteRecordDbHelper.KEY_BOARD_1_TIME, mTimestamps.get(Step.BOARD_1));
        values.put(CommuteRecordDbHelper.KEY_ALIGHT_1_TIME, mTimestamps.get(Step.ALIGHT_1));
        values.put(CommuteRecordDbHelper.KEY_AT_STOP_2_TIME, mTimestamps.get(Step.AT_STOP_2));
        values.put(CommuteRecordDbHelper.KEY_BOARD_2_TIME, mTimestamps.get(Step.BOARD_2));
        values.put(CommuteRecordDbHelper.KEY_ALIGHT_2_TIME, mTimestamps.get(Step.ALIGHT_2));
        values.put(CommuteRecordDbHelper.KEY_ARRIVE_TIME, mTimestamps.get(Step.ARRIVE));
        values.put(CommuteRecordDbHelper.KEY_ROUTE_1_NAME, mRoute.toString());
        if (mTransferRoute != null) {
            values.put(CommuteRecordDbHelper.KEY_ROUTE_2_NAME, mTransferRoute.toString());
        }
        if (mDirection != null) {
            switch (mDirection) {
                case WORK:
                case HOME:
                    values.put(CommuteRecordDbHelper.KEY_DIRECTION, mDirection.ordinal());
                    break;
                default:
                    values.put(CommuteRecordDbHelper.KEY_DIRECTION, 2); //TODO 2 = other, make a constant for this somewhere logical
                    break;
            }
        }
        return values;
    }

    public void recordTime(Step s) {
        if (mTimestamps.containsKey(s)) {
            throw new IllegalStateException(s + " already present in this record");
        }
        mTimestamps.put(s, System.currentTimeMillis());
    }

    public void setRoute(Route r) {
        mRoute = r;
    }

    public void setTransferRoute(Route r) {
        mTransferRoute = r;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }
}
