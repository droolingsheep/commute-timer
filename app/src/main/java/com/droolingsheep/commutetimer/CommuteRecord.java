package com.droolingsheep.commutetimer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asbransc on 3/20/15.
 */
public class CommuteRecord {

    private static CommuteRecord sInstance;

    private Map<Step, Long> mTimestamps = new HashMap<>();
    private Route mRoute;

    public static CommuteRecord getInstance() {
        return sInstance;
    }

    public static void newInstance() {
        if (sInstance != null) {
            //TODO save this instance somewhere
        }
        sInstance = new CommuteRecord();
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
}
