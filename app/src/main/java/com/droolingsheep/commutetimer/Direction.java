package com.droolingsheep.commutetimer;

/**
* Created by asbransc on 3/13/15.
*/
public enum Direction {
    WORK(false), HOME(false), WORK_TRANSFER(true), HOME_TRANSFER(true);

    private boolean mTransfer;

    private Direction(boolean isTransfer) {
        mTransfer = isTransfer;
    }

    boolean isTransfer() {
        return mTransfer;
    }
}
