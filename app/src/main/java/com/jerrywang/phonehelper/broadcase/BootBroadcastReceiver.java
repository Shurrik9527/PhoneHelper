package com.jerrywang.phonehelper.broadcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.service.LoadAppListService;
import com.jerrywang.phonehelper.service.LockService;
import com.jerrywang.phonehelper.util.SpHelper;
import org.litepal.util.LogUtil;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 广播服务
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG =BootBroadcastReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG,"开机启动服务....");
        //加载应用服务

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context, LoadAppListService.class));
        }else {
            context.startService(new Intent(context, LoadAppListService.class));
        }

        //应用锁启动服务
        boolean lock_start = (boolean) SpHelper.getInstance().get(Constant.LOCK_STATE, false);
        if (lock_start) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(new Intent(context, LockService.class));
            }else {
                context.startService(new Intent(context, LockService.class));
            }
        }
    }

}
