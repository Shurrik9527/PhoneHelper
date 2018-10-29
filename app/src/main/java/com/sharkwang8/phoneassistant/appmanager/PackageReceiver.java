package com.sharkwang8.phoneassistant.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sharkwang8.phoneassistant.event.UninstallEvent;
import com.sharkwang8.phoneassistant.util.RxBus.RxBus;

public class PackageReceiver extends BroadcastReceiver {
    public PackageReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            RxBus.getDefault().post(new UninstallEvent());
        }
    }
}
