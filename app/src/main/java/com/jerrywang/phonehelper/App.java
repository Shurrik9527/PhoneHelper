package com.jerrywang.phonehelper;

import android.app.Application;
import android.content.Context;

import com.jerrywang.phonehelper.manager.CleanManager;
import com.jerrywang.phonehelper.manager.JunkCleanerManager;
import com.jerrywang.phonehelper.manager.MemoryManager;
import com.jerrywang.phonehelper.manager.ProcessManager;

/**
 * Created by Shurrik on 2017/11/29.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext =this;

        MemoryManager.init(this);
        ProcessManager.init(getApplicationContext());
        CleanManager.init(this);
        JunkCleanerManager.init(this);
    }


    public static Context getmContext() {
        return mContext;
    }
}
