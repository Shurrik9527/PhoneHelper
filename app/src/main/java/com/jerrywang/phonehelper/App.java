package com.jerrywang.phonehelper;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shurrik on 2017/11/29.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //init sth. here
        this.mContext =this;
    }


    public static Context getmContext() {
        return mContext;
    }
}
