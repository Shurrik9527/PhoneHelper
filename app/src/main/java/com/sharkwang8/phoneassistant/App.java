package com.sharkwang8.phoneassistant;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.crabsdk.CrabSDK;
import com.sharkwang8.phoneassistant.base.Constant;
import com.sharkwang8.phoneassistant.exception.CustomCrashHandler;
import com.sharkwang8.phoneassistant.manager.AddressListManager;
import com.sharkwang8.phoneassistant.manager.CallLogManager;
import com.sharkwang8.phoneassistant.manager.CleanManager;
import com.sharkwang8.phoneassistant.manager.JunkCleanerManager;
import com.sharkwang8.phoneassistant.manager.MemoryManager;
import com.sharkwang8.phoneassistant.manager.ProcessManager;
import com.sharkwang8.phoneassistant.manager.SMSManager;
import com.sharkwang8.phoneassistant.service.LoadAppListService;
import com.sharkwang8.phoneassistant.service.LockService;
import com.sharkwang8.phoneassistant.util.SpHelper;

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
        CrabSDK.init(this,Constant.BAIDU_KEY);
        // 开启卡顿捕获功能, 传入每天上传卡顿信息个数，-1代表不限制, 已自动打开
        CrabSDK.enableBlockCatch(-1);
        /* 初始化全局异常捕获信息 */
        CustomCrashHandler customCrashHandler = CustomCrashHandler.getInstance();
        customCrashHandler.init(this);
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
