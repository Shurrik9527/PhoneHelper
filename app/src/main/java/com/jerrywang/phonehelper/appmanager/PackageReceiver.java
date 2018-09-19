package com.jerrywang.phonehelper.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jerrywang.phonehelper.event.UninstallEvent;
import com.jerrywang.phonehelper.util.RxBus.RxBus;

public class PackageReceiver extends BroadcastReceiver {
    public PackageReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            RxBus.getDefault().post(new UninstallEvent());
        }
    }
}
