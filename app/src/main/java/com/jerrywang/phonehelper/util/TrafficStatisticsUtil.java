package com.jerrywang.phonehelper.util;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jerrywang.phonehelper.bean.AppInformBean;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.jerrywang.phonehelper.util.TimeUtil.getTimesMonthMorning;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/31
 * @email 252774645@qq.com
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class TrafficStatisticsUtil {

    private static final String TAG = TrafficStatisticsUtil.class.getName();

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
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        String subId = tm.getSubscriberId();

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            NetworkStats.Bucket wifibucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
            long wifiTotal = wifibucket.getRxBytes() + wifibucket.getTxBytes();


            NetworkStats.Bucket mobilebucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
            long mobileTotal = mobilebucket.getRxBytes() + mobilebucket.getTxBytes();
            Log.i(TAG, "mobileTotal==" + mobileTotal);

            return wifiTotal+mobileTotal;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisticsBean> getAppTrafficStatistics(Context context) {
        if (context == null)
            return null;


        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        List<TrafficStatisticsBean> wifiLists = new ArrayList<>();
        List<TrafficStatisticsBean> mobileLists = new ArrayList<>();
        //wifi 使用
        try {
            NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
            NetworkStats wifiStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
            do {
                wifiStats.getNextBucket(wifiBucket);
                int summaryUid = wifiBucket.getUid();
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                TrafficStatisticsBean bean = new TrafficStatisticsBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setWifiSize(size);
                wifiLists.add(bean);

            } while (wifiStats.hasNextBucket());

            //mobile 使用

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

            NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
            NetworkStats mobileStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE,subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
            if(mobileStats==null)
                return null;
            do {
                mobileStats.getNextBucket(mobileBucket);
                int summaryUid = mobileBucket.getUid();
                long size = mobileBucket.getRxBytes() + mobileBucket.getTxBytes();
                TrafficStatisticsBean bean = new TrafficStatisticsBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setMobileSize(size);
                mobileLists.add(bean);

            } while (mobileStats.hasNextBucket());


            //去重
            List<TrafficStatisticsBean> wifiList =removeDuplicate(wifiLists);
            List<TrafficStatisticsBean> mobileList =removeDuplicate(mobileLists);

            for(TrafficStatisticsBean bean :mobileList){
                Log.i(TAG,"mobile===="+bean.toString());
            }

            for(TrafficStatisticsBean bean :wifiList){
                Log.i(TAG,"wifiList===="+bean.toString());
            }

            //合并
            List<TrafficStatisticsBean> lastLists =combine(wifiList,mobileList);
            //过滤未使用的流量app
            List<TrafficStatisticsBean> mlist =filterApp(context,lastLists);
            Collections.sort(mlist, new Comparator<TrafficStatisticsBean>() {
                @Override
                public int compare(TrafficStatisticsBean o1, TrafficStatisticsBean o2) {
                    if(o2.getTotalSize()-o1.getTotalSize()==0){
                        return 0;
                    }else if(o2.getTotalSize()-o1.getTotalSize()>0){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
            return mlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisticsBean> getTotalAppTrafficStatistics(Context context) {
        if (context == null)
            return null;

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
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
        List<TrafficStatisticsBean> mLists = new ArrayList<>();
        for (PackageInfo mbean : mList) {
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.packageName);
                NetworkStats wifiStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, subId, getTimesMonthMorning(), System.currentTimeMillis(), uid);
                wifiStats.getNextBucket(wifiBucket);
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                wifiStats.close();
                TrafficStatisticsBean bean = new TrafficStatisticsBean();
                bean.setmName(AppUtil.getApplicationName(context, mbean.applicationInfo));
                bean.setmDrawable(AppUtil.getIconByPkgname(context, mbean.packageName));
                bean.setmPackageName(mbean.packageName);
                bean.setWifiSize(size);
                mLists.add(bean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


        NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
        for (int i = 0; i < mLists.size(); i++) {
            TrafficStatisticsBean mbean = mLists.get(i);
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.getmPackageName());
                NetworkStats mobileStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, subId, getTimesMonthMorning(), System.currentTimeMillis(), uid);
                mobileStats.getNextBucket(mobileBucket);
                mobileStats.close();
                long size = mobileBucket.getRxBytes() + mobileBucket.getTxBytes();
                mbean.setMobileSize(size);
                mLists.set(i, mbean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        return mLists;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisticsBean> getAppMobileTrafficStatistics(Context context) {
        if (context == null)
            return null;

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
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
        List<TrafficStatisticsBean> mLists = new ArrayList<>();
        for (PackageInfo mbean : mList) {
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.packageName);
                NetworkStats wifiStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, subId, getTimesMonthMorning(), System.currentTimeMillis(), uid);
                wifiStats.getNextBucket(wifiBucket);
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                TrafficStatisticsBean bean = new TrafficStatisticsBean();
                bean.setmName(AppUtil.getApplicationName(context, mbean.applicationInfo));
                bean.setmDrawable(AppUtil.getIconByPkgname(context, mbean.packageName));
                bean.setmPackageName(mbean.packageName);
                bean.setWifiSize(size);
                mLists.add(bean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    /**
     * 手机当天网络流量
     * @param context
     * @return
     */
    public long getAllTodayMobile(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), TimeUtil.getTimesmorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 手机当天wifi流量
     * @param context
     * @return
     */
    public long getAllTodayWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, getSubscriberId(context), TimeUtil.getTimesmorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 手机当月网络流量
     * @param context
     * @return
     */
    public long getAllMonthMobile(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getRxBytes() + bucket.getTxBytes();
    }


    /**
     * 手机当月wifi 流量
     * @param context
     * @return
     */
    public long getAllMonthWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, getSubscriberId(context), TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getRxBytes() + bucket.getTxBytes();
    }

    /**
     * 开机到现在wifi s所有接受流量
     * @param context
     * @return
     */
    public static long getAllRxBytesWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
            return bucket.getRxBytes();
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在wifi 所有发送流量
     * @param context
     * @return
     */
    public static long getAllTxBytesWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
           long size =bucket.getTxBytes();
           return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在网络 所有接受流量
     * @param context
     * @return
     */
    public static long getPackageRxBytesMobile(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getRxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在网络 所有发送流量
     * @param context
     * @return
     */
    public static long getPackageTxBytesMobile(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getTxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在wifi 某个包所有接受流量
     * @param context
     * @return
     */
    public static long getPackageRxBytesWifi(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getRxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }



    /**
     * 开机到现在wifi 某个包所有发送流量
     * @param context
     * @return
     */
    public static long getPackageTxBytesWifi(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getTxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }


//    /**
//     * 开机到现在mobile 某个包所有发送流量
//     * @param context
//     * @return
//     */
//    public static long getPackageTxBytesMobile(Context context,int packageUid) {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
//        NetworkStats networkStats = null;
//        try {
//            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis(), packageUid);
//            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
//            networkStats.getNextBucket(bucket);
//            long size =bucket.getTxBytes();
//            networkStats.close();
//            return size;
//        } catch (RemoteException e) {
//            return 0;
//        }
//    }
//
//    /**
//     * 开机到现在mobile 某个包所有接受流量
//     * @param context
//     * @return
//     */
//    public static long getPackageRxBytesMobile(Context context,int packageUid) {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
//        NetworkStats networkStats = null;
//        try {
//            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis(), packageUid);
//            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
//            networkStats.getNextBucket(bucket);
//            long size =bucket.getRxBytes();
//            networkStats.close();
//            return size;
//        } catch (RemoteException e) {
//            return 0;
//        }
//    }



    private static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //
        //            final String actualSubscriberId = tm.getSubscriberId();
//            return SystemProperties.get(TEST_SUBSCRIBER_PROP, actualSubscriberId);
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
        return tm.getSubscriberId();
    }

    /**
     * 排除重复的数据
     * @param trafficStatisticsBeans
     * @return
     */
    private static List<TrafficStatisticsBean> removeDuplicate(List<TrafficStatisticsBean> trafficStatisticsBeans){
        for (int i =0;i<trafficStatisticsBeans.size()-1;i++){
            TrafficStatisticsBean o1 =trafficStatisticsBeans.get(i);
            for (int j=trafficStatisticsBeans.size()-1;j>i;j--){
                TrafficStatisticsBean o2 =trafficStatisticsBeans.get(j);
                if(o1.getUid().compareTo(o2.getUid())==0){
                    long wifisize =o1.getWifiSize()+o2.getWifiSize();
                    long mobilesize =o1.getMobileSize()+o2.getMobileSize();
                    o1.setWifiSize(wifisize);
                    o1.setMobileSize(mobilesize);
                    trafficStatisticsBeans.set(i,o1);
                    trafficStatisticsBeans.remove(j);
                }
            }
        }
        return trafficStatisticsBeans;
    }

    /**
     * 合并
     * @param wifibean
     * @param mobilebean
     * @return
     */
    private static List<TrafficStatisticsBean> combine(List<TrafficStatisticsBean> wifibean,List<TrafficStatisticsBean> mobilebean){

        if((wifibean==null||wifibean.size()==0)&&(mobilebean==null||mobilebean.size()==0)){
            return null;
        }

        if((wifibean!=null&&wifibean.size()>0)&&(mobilebean==null||mobilebean.size()==0)){
            return wifibean;
        }

        if((wifibean==null||wifibean.size()==0)&&(mobilebean!=null&&mobilebean.size()>0)){
            return mobilebean;
        }
        wifibean.addAll(mobilebean);
        return removeDuplicate(wifibean);

    }

    /**
     * 帅选 有流浪的APP
     * @param lists
     * @return
     */
    private static List<TrafficStatisticsBean> filterApp(Context context,List<TrafficStatisticsBean> lists){

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
        if(mList==null||mList.size()==0){
            return null;
        }
        if(lists==null||lists.size()==0){
            return null;
        }

        for (int i =0;i<mList.size();i++){
            PackageInfo bean =mList.get(i);
            int uid = AppUtil.getUidByPackageName(context,bean.packageName);
            for (int j=0;j<lists.size();j++){
                TrafficStatisticsBean bean1 =lists.get(j);
                if(Integer.parseInt(bean1.getUid())==0){
                    lists.remove(j);
                    continue;
                }
                bean1.setTotalSize(bean1.getWifiSize()+bean1.getMobileSize());
                if(Integer.parseInt(bean1.getUid())==uid){
                    bean1.setmName(AppUtil.getApplicationName(context,bean.applicationInfo)+"");
                    bean1.setmDrawable(AppUtil.getIconByPkgname(context,bean.packageName));
                    bean1.setmPackageName(bean.packageName);
                    lists.set(j,bean1);
                }
            }
        }
        return lists;
    }



}
