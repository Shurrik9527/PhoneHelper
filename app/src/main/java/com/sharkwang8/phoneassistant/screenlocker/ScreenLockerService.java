package com.sharkwang8.phoneassistant.screenlocker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.sharkwang8.phoneassistant.util.AdUtil;
import com.sharkwang8.phoneassistant.util.SharedPreferencesHelper;

public class ScreenLockerService extends Service {
    private SharedPreferencesHelper sharedPreferencesHelper;

    //屏幕熄灭的广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
                boolean isProtect = Boolean.parseBoolean(sharedPreferencesHelper.getSharedPreference("isProtect", false).toString().trim());
                if (isProtect) {
                    Intent lockScreenIntent = new Intent(ScreenLockerService.this, ScreenLockerActivity.class);
                    lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //开启锁屏界面
                    startActivity(lockScreenIntent);
                }

                //每1小时允许展示一次广告
                if (System.currentTimeMillis() - AdUtil.SHOW_TIME > 3600000) {
                    //启动FaceBoo广告
                    AdUtil.showFacebookAds(ScreenLockerService.this);
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
        sharedPreferencesHelper = new SharedPreferencesHelper(ScreenLockerService.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("id","name", NotificationManager.IMPORTANCE_LOW);
            final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(getApplicationContext(),"id").build();
            startForeground(1, notification);
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();

        Intent localIntent = new Intent();
        localIntent.setClass(this, ScreenLockerService.class); //销毁时重新启动Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            this.startForegroundService(localIntent);
        }else {
            this.startService(localIntent);
        }
    }

}

