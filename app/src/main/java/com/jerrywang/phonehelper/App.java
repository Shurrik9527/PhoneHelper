package com.jerrywang.phonehelper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.manager.AddressListManager;
import com.jerrywang.phonehelper.manager.CallLogManager;
import com.jerrywang.phonehelper.manager.CleanManager;
import com.jerrywang.phonehelper.manager.JunkCleanerManager;
import com.jerrywang.phonehelper.manager.MemoryManager;
import com.jerrywang.phonehelper.manager.ProcessManager;
import com.jerrywang.phonehelper.manager.SMSManager;
import com.jerrywang.phonehelper.service.LoadAppListService;
import com.jerrywang.phonehelper.service.LockService;
import com.jerrywang.phonehelper.util.SpHelper;

import org.litepal.LitePalApplication;

/**
 * Created by Shurrik on 2017/11/29.
 */

public class App extends LitePalApplication {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext =this;

        MemoryManager.init(this);
        ProcessManager.init(getApplicationContext());
        CleanManager.init(this);
        JunkCleanerManager.init(this);
        SpHelper.getInstance().init(this);
        AddressListManager.init(getApplicationContext());
        CallLogManager.init(getApplicationContext());
        SMSManager.init(getApplicationContext());

        initServices();
    }

    /**
     * 初始化服务
     */
    private void initServices() {
        startService(new Intent(this, LoadAppListService.class));
        boolean lockState = (boolean) SpHelper.getInstance().get(Constant.LOCK_STATE, false);
        if (lockState) {
            startService(new Intent(this, LockService.class));
        }
    }


    public static Context getmContext() {
        return mContext;
    }
}
