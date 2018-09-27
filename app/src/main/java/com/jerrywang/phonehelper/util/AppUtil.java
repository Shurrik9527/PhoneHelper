package com.jerrywang.phonehelper.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.jerrywang.phonehelper.bean.AppInformBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     * @param context
     * @param filterSystem
     * @return
     */
    public static Observable<List<AppInformBean>> getInstalledAppInformBeanUsingObservable(final Context context, final boolean filterSystem) {

       return Observable.create(new ObservableOnSubscribe<List<AppInformBean>>() {
           @Override
           public void subscribe(ObservableEmitter<List<AppInformBean>> e) throws Exception {
               e.onNext(getInstalledApplicationInfo(context,filterSystem));
               e.onComplete();
           }
       }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }




    /**
     * 获取已经安装的应用
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
                        appInformBean.setmDrawable(getIconByPkgname(context,info.packageName));
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
                appInformBean.setmDrawable(getIconByPkgname(context,info.packageName));
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
}
