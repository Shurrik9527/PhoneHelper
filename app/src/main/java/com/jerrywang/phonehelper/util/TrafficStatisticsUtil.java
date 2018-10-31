package com.jerrywang.phonehelper.util;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jerrywang.phonehelper.bean.AppInformBean;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;

import java.util.ArrayList;
import java.util.List;

import static com.jerrywang.phonehelper.util.TimeUtil.getTimesMonthMorning;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/31
 * @email 252774645@qq.com
 */
public class TrafficStatisticsUtil {


    /**
     * 获取当月所有流量
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static long getTotalTrafficStatistics(Context context) {
        if (context == null) {
            return 0;
        }
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            NetworkStats.Bucket bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", getTimesMonthMorning(), System.currentTimeMillis());
            long mTotal = bucket.getRxBytes() + bucket.getTxBytes();
            return mTotal;
        } catch (RemoteException e) {
            e.printStackTrace();

        }

        return 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisticsBean> getAppTrafficStatistics(Context context) {
        if (context == null)
            return null;

        List<AppInformBean> mList = AppUtil.getInstalledApplicationInfo(context, false);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String subId = tm.getSubscriberId();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
        List<TrafficStatisticsBean> mLists =new ArrayList<>();
        try {
            NetworkStats wifiStats=networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, subId, getTimesMonthMorning(), System.currentTimeMillis());
            do {
                wifiStats.getNextBucket(wifiBucket);
                int summaryUid = wifiBucket.getUid();
                for (AppInformBean mbean :mList){
                    if (AppUtil.getUidByPackageName(context,mbean.getmPackageName()) == summaryUid) {
                        long size =wifiBucket.getRxBytes()+wifiBucket.getTxBytes();
                        TrafficStatisticsBean bean = new TrafficStatisticsBean();
                        bean.setmName(mbean.getmName());
                        bean.setmDrawable(mbean.getmDrawable());
                        bean.setmPackageName(mbean.getmPackageName());
                        if(size==0){
                            bean.setWifiSize("0");
                        }else{
                            bean.setWifiSize(String.valueOf(size));
                        }
                        mLists.add(bean);
                    }

                }

            } while (wifiStats.hasNextBucket());

            NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
            NetworkStats mobileStats=networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, subId, getTimesMonthMorning(), System.currentTimeMillis());
            do {
                mobileStats.getNextBucket(mobileBucket);
                int summaryUid = mobileBucket.getUid();
                for (int i=0;i<mLists.size();i++){
                    TrafficStatisticsBean mbean =mLists.get(i);
                    if (AppUtil.getUidByPackageName(context,mbean.getmPackageName()) == summaryUid) {
                        long size =mobileBucket.getRxBytes()+mobileBucket.getTxBytes();
                        if(size==0){
                            mbean.setMobileSize("0");
                        }else{
                            mbean.setMobileSize(String.valueOf(size));
                        }
                        mLists.set(i,mbean);
                    }
                }

            } while (mobileStats.hasNextBucket());

            return mLists;

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }



}
