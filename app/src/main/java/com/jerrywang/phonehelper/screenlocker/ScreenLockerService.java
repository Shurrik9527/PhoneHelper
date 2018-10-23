package com.jerrywang.phonehelper.screenlocker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.jerrywang.phonehelper.util.SharedPreferencesHelper;

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
        this.startService(localIntent);
    }

}

