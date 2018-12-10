package com.sharkwang8.phoneassistant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import android.os.Build;

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
import com.sharkwang8.phoneassistant.service.HideAppService;
import com.sharkwang8.phoneassistant.service.LoadAppListService;
import com.sharkwang8.phoneassistant.service.LockService;
import com.sharkwang8.phoneassistant.util.SpHelper;

import org.litepal.LitePalApplication;

import java.util.Map;

/**
 * Created by Shurrik on 2017/11/29.
 */

public class App extends LitePalApplication {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;

        MemoryManager.init(this);
        ProcessManager.init(getApplicationContext());
        CleanManager.init(this);
        JunkCleanerManager.init(this);
        SpHelper.getInstance().init(this);
        AddressListManager.init(getApplicationContext());
        CallLogManager.init(getApplicationContext());
        SMSManager.init(getApplicationContext());

        initServices();

        initAppsFlyer();

        initBaiduCrab();
    }

    /**
     * 初始化服务
     */
    private void initServices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LoadAppListService.class));
        } else {
            startService(new Intent(this, LoadAppListService.class));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, HideAppService.class));
        } else {
            startService(new Intent(this, HideAppService.class));
        }


        boolean lockState = (boolean) SpHelper.getInstance().get(Constant.LOCK_STATE, false);
        if (lockState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, LockService.class));
            } else {
                startService(new Intent(this, LockService.class));
            }
        }

    }

    /**
     * 初始化AppsFlyer
     */
    private void initAppsFlyer() {
        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {
                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                        for (String attrName : conversionData.keySet()) {
                            Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                        }
                    }

                    @Override
                    public void onInstallConversionFailure(String errorMessage) {
                        Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> conversionData) {

                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {
                        Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
                    }
                };
        //Your dev key is accessible from the AppsFlyer Dashboard under the Configuration section inside App Settings
        final String AF_DEV_KEY = "gCjmRfaYsA8JeaeWR6GQyX";
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }

    /**
     * 初始化百度Crab
     */
    private void initBaiduCrab() {
        CrabSDK.init(this, Constant.BAIDU_KEY);
        // 开启卡顿捕获功能, 传入每天上传卡顿信息个数，-1代表不限制, 已自动打开
        CrabSDK.enableBlockCatch(-1);
        CrabSDK.setUploadLimitOfSameCrashInOneday(-1);
        CrabSDK.setUploadLimitOfCrashInOneday(-1);
        CrabSDK.setBlockThreshold(2000);
        /* 初始化全局异常捕获信息 */
        CustomCrashHandler customCrashHandler = CustomCrashHandler.getInstance();
        customCrashHandler.init(this);
    }


    public static Context getmContext() {
        return mContext;
    }
}
