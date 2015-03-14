package com.droolingsheep.commutetimer;

/**
* Created by asbransc on 3/13/15.
*/
public enum Route {
    FILL("fill"),
    IN("in"),
    ROUTES("routes"),
    HERE("here");

    Route(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }

    private String mName;

    public static Route[] sWorkRoutes = {

    };

    public static Route[] sHomeRoutes = {

    };

    public static Route[] sWorkTransferRoutes = {

    };

    public static Route[] sHomeTransferRoutes = {

    };
}
