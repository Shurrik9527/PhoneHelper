package com.gochicken3.mobilehelper.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gochicken3.mobilehelper.EmptyActivity;
import com.gochicken3.mobilehelper.event.UninstallEvent;
import com.gochicken3.mobilehelper.util.AdUtil;
import com.gochicken3.mobilehelper.util.RxBus.RxBus;

public class PackageReceiver extends BroadcastReceiver {
    public PackageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            RxBus.getDefault().post(new UninstallEvent());
            //广告
            //AdUtil.showAds(context, "PackageReceiver.onReceive()");
            if (AdUtil.IS_SHOW_AD) {
                Intent emptyIntent = new Intent();
                emptyIntent.setClass(context, EmptyActivity.class);
                emptyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emptyIntent);
            }
        }
    }
}
