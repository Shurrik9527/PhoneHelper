package com.sharkwang8.phoneassistant.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.sharkwang8.phoneassistant.bean.AppInformBean;

import java.io.File;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author heguogui
 * @describe app管理工具
 * @date 2018/9/5
 * @email 252774645@qq.com
 */
public class AppUtil {

    //获取已经安装的应用
    public static List<PackageInfo> getInstalledPackages(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<PackageInfo> pakList = new ArrayList<>();

        try {
            pakList = packageManager.getInstalledPackages(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pakList;
    }


    /**
     * 异步订阅
     *
     * @param context
     * @param filterSystem
     * @return
     */
    public static Observable<List<AppInformBean>> getInstalledAppInformBeanUsingObservable(final Context context, final boolean filterSystem) {

        return Observable.create(new ObservableOnSubscribe<List<AppInformBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInformBean>> e) throws Exception {
                e.onNext(getInstalledApplicationInfo(context, filterSystem));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取已经安装的应用
     *
     * @param context
     * @param filterSystem 是否过滤是系统应用
     * @return
     */
    public static List<AppInformBean> getInstalledApplicationInfo(Context context, boolean filterSystem) {
        List<PackageInfo> tempList = getInstalledPackages(context);
        List<AppInformBean> pakList = new ArrayList<>();
        for (PackageInfo info : tempList) {
            AppInformBean appInformBean = new AppInformBean();
            ApplicationInfo applicationInfo = info.applicationInfo;
            if (filterSystem) {
                if (!isSystemApp(applicationInfo)) {
                    if (!isAppDisable(context, info.packageName)) {
                        appInformBean.setmPackageInfo(info);
                        appInformBean.setmPackageName(info.packageName);
                        appInformBean.setmName(getApplicationName(context, applicationInfo));
                        appInformBean.setmDrawable(getIconByPkgname(context, info.packageName));
                        appInformBean.setmSize(getAppSize(applicationInfo));
                        appInformBean.setmIsSystem(false);
                        appInformBean.setmInstallTime(getInstallTime(applicationInfo));
                        pakList.add(appInformBean);
                    }
                }
            } else {
                appInformBean.setmPackageInfo(info);
                appInformBean.setmPackageName(info.packageName);
                appInformBean.setmPackageName(info.packageName);
                appInformBean.setmName(getApplicationName(context, applicationInfo));
                appInformBean.setmDrawable(getIconByPkgname(context, info.packageName));
                appInformBean.setmSize(getAppSize(applicationInfo));
                appInformBean.setmInstallTime(getInstallTime(applicationInfo));
                pakList.add(appInformBean);
            }
        }

        //排序
        Collections.sort(pakList);
        return pakList;
    }

    /**
     * 获取应用名字
     *
     * @param context
     * @param info
     * @return
     */
    public static String getApplicationName(Context context, ApplicationInfo info) {
        PackageManager pkgManager = context.getApplicationContext().getPackageManager();
        return info.loadLabel(pkgManager).toString();
    }

    /**
     * 获取应用ICON图标
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static Drawable getIconByPkgname(Context context, String pkgName) {
        if (pkgName != null) {
            PackageManager pkgManager = context.getApplicationContext().getPackageManager();
            try {
                PackageInfo pkgInfo = pkgManager.getPackageInfo(pkgName, 0);
                return pkgInfo.applicationInfo.loadIcon(pkgManager);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断应用是否是系统应用
     *
     * @param info
     * @return
     */
    public static boolean isSystemApp(ApplicationInfo info) {
        boolean isSystemApp = false;
        if (info != null) {
            isSystemApp = (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                    || (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        }
        return isSystemApp;
    }

    /**
     * 获取安装时间
     *
     * @param info
     * @return
     */
    public static long getInstallTime(ApplicationInfo info) {
        String sourceDir;
        try {
            sourceDir = info.sourceDir;
            File file = new File(sourceDir);
            return file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * app 大小
     *
     * @param info
     * @return
     */
    public static long getAppSize(ApplicationInfo info) {
        String publicSourceDir;
        try {
            publicSourceDir = info.publicSourceDir;
            File file = new File(publicSourceDir);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * App 是否禁用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppDisable(Context context, String packageName) {
        boolean isDisable = false;
        try {
            ApplicationInfo app = context.getPackageManager()
                    .getApplicationInfo(packageName, 0);
            isDisable = !app.enabled;
        } catch (Exception e) {
            isDisable = true;
            e.printStackTrace();
        }
        return isDisable;
    }


    /**
     * 判断GooglePlay是否已经安装在设备上
     */
    public static boolean isInstalled(Context context, String packageName) {
        try {
            context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前手机IP地址
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                // Convert little-endian to big-endianif needed
                if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                    ipAddress = Integer.reverseBytes(ipAddress);
                }

                byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

                String ipAddressString;
                try {
                    ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
                } catch (UnknownHostException ex) {
                    Log.e("IP", "Unable to get host address.");
                    ipAddressString = null;
                }

                return ipAddressString;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            Log.e("IP", "Unable connect to the Internet.");
        }
        return null;
    }

    /**
     * 获取Google Advertising ID
     */
    public static String getAid(Context context) throws Exception {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return "Cannot call in the main thread, You must call in the other thread";
        }
        PackageManager pm = context.getPackageManager();
        pm.getPackageInfo("com.android.vending", 0);
        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent(
                "com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            try {
                AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                return adInterface.getId();
            } finally {
                context.unbindService(connection);
            }
        }
        return "";
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);

        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (InterruptedException localInterruptedException) {
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved)
                throw new IllegalStateException();
            this.retrieved = true;
            return this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }

        public IBinder asBinder() {
            return binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean)
                throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean limitAdTracking;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(paramBoolean ? 1 : 0);
                binder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = 0 != reply.readInt();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }

    public static List<ApplicationInfo> getUnInstalledApplicationInfo(Context context) {
        
        PackageManager packageManager = context.getPackageManager(); // 获得PackageManager对象
        List<ApplicationInfo> listAppcations = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        if(listAppcations==null||listAppcations.size()==0){
            return null;
        }
        List<ApplicationInfo> mData = new ArrayList<ApplicationInfo>();
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(packageManager));// 字典排序
        for (ApplicationInfo app : listAppcations) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                //非系统程序
                mData.add(app);
            } else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                //data.add(app);
            }
        }
        return mData;
    }




}
