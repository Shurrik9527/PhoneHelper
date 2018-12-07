package com.sharkwang8.phoneassistant.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sharkwang8.phoneassistant.EmptyActivity;
import com.sharkwang8.phoneassistant.event.UninstallEvent;
import com.sharkwang8.phoneassistant.util.RxBus.RxBus;

public class PackageReceiver extends BroadcastReceiver {
    public PackageReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            RxBus.getDefault().post(new UninstallEvent());
            //广告
            //AdUtil.showAds(context, "PackageReceiver.onReceive()");
            Intent emptyIntent = new Intent();
            emptyIntent.setClass(context, EmptyActivity.class);
            emptyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(emptyIntent);
        }
    }
}
