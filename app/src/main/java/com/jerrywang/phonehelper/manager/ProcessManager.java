package com.jerrywang.phonehelper.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jerrywang.phonehelper.App;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 运行进程管理
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class ProcessManager {

    private  static  final  String TAG =ProcessManager.class.getName();
    private static ProcessManager mInstance;
    private Context mContext;
    private List<AppProcessInfornBean> mTempList;  //暂时保存进程列表
    private List<AppProcessInfornBean> mRunningProcessList;
    private PackageManager mPackageManager;
    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInform;

    private ProcessManager(Context context) {
        this.mContext = context;
        mRunningProcessList = new ArrayList<>();
        mPackageManager = mContext.getPackageManager();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mMemoryInform = new ActivityManager.MemoryInfo();
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (ProcessManager.class) {
                if (mInstance == null) {
                    mInstance = new ProcessManager(context);
                }
            }
        }
    }

    public static ProcessManager getInstance() {

        if (mInstance == null) {
            throw new IllegalStateException("ProcessManager is not init");
        }
        return mInstance;
    }

    public List<AppProcessInfornBean> getRunningProcessList(boolean isSelect) {

        if(mContext==null)
            return new ArrayList<AppProcessInfornBean>();

        mTempList = new ArrayList<>();
        ApplicationInfo appInfo = null;
        AppProcessInfornBean abAppProcessInfornBean = null;

        for (ActivityManager.RunningAppProcessInfo info : AndroidProcesses.getRunningAppProcessInfo(mContext)) {

            if (!info.processName.equals(mContext.getPackageName())) {
                abAppProcessInfornBean = new AppProcessInfornBean(info.processName, info.pid, info.uid);
                try {
                    appInfo = mPackageManager.getApplicationInfo(info.processName, 0);
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        abAppProcessInfornBean.setSystem(true);
                    } else {
                        abAppProcessInfornBean.setSystem(false);
                    }
                    Drawable icon = appInfo.loadIcon(mPackageManager) == null ?
                            ActivityCompat.getDrawable(mContext, R.mipmap.ic_launcher)
                            : appInfo.loadIcon(mPackageManager);
                    String name = appInfo.loadLabel(mPackageManager).toString();
                    abAppProcessInfornBean.setIcon(icon);
                    abAppProcessInfornBean.setAppName(name);
                } catch (PackageManager.NameNotFoundException e) {
                    /*名字没找到，可能是应用的服务*/
                    if (info.processName.contains(":")) {
                        appInfo = getApplicationInfo(info.processName.split(":")[0]);
                        if (appInfo != null) {
                            Drawable icon = appInfo.loadIcon(mPackageManager);
                            abAppProcessInfornBean.setIcon(icon);
                        } else {
                            abAppProcessInfornBean.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }
                    abAppProcessInfornBean.setSystem(true);
                    abAppProcessInfornBean.setAppName(info.processName);
                }

                long memsize = mActivityManager.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
                abAppProcessInfornBean.setMemory(memsize);

                if(isSelect){
                    if (!abAppProcessInfornBean.isSystem()) {
                        mTempList.add(abAppProcessInfornBean);
                    }
                }else{
                    mTempList.add(abAppProcessInfornBean);
                }

            }

        }

        //APP去重
        ComoaratorApp comparator = new ComoaratorApp();
        Collections.sort(mTempList, comparator);
        int lastUid = 0;
        int index = -1;
        mRunningProcessList.clear();
        //
        for (AppProcessInfornBean info : mTempList) {
            if (lastUid == info.getU_id()) {
                AppProcessInfornBean nowInfo = mTempList.get(index);
                mRunningProcessList.get(index).setMemory(nowInfo.getMemory() + info.getMemory());
            } else {
                index++;
                mRunningProcessList.add(info);
                lastUid = info.getU_id();
            }
        }
        return mTempList;
    }

    public ApplicationInfo getApplicationInfo(String processName) {
        if (processName == null) {
            return null;
        }
        List<ApplicationInfo> appList = mPackageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }


    /**
     * 订阅 除了本身当前运行的进程
     * @return
     */
    public Observable<List<AppProcessInfornBean>> getRunningAppListObservable(final boolean isSelect){
        return  Observable.create(new ObservableOnSubscribe<List<AppProcessInfornBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppProcessInfornBean>> e) throws Exception {
                e.onNext(getRunningProcessList(isSelect));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 返回进程列表
     * @return
     */
    public List<AppProcessInfornBean> getRunningAppList() {
        return mRunningProcessList;
    }

    /**
     * 杀死所有列表 获取当前可用内存
     * @return
     */
    public long killAllRunningApp(boolean select) {
        long beforeMemory = 0;
        long endMemory = 0;
        mActivityManager.getMemoryInfo(mMemoryInform);
        beforeMemory = mMemoryInform.availMem;

        for (AppProcessInfornBean info : getRunningProcessList(select)) {
            killBackgroundProcesses(info.getProcessName());
        }

        mActivityManager.getMemoryInfo(mMemoryInform);
        endMemory = mMemoryInform.availMem;
        return endMemory - beforeMemory;
    }


    /**
     * 杀死指定进程 获取当前可用内存
     * @param processName
     * @return
     */
    public long killRunningApp(String processName) {

        long beforeMemory = 0;
        long endMemory = 0;

        mActivityManager.getMemoryInfo(mMemoryInform);
        beforeMemory = mMemoryInform.availMem;

        try {

            killBackgroundProcesses(processName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mActivityManager.getMemoryInfo(mMemoryInform);
        endMemory = mMemoryInform.availMem;

        return endMemory - beforeMemory;
    }

    /**
     * 杀死指定进程
     * @param processName
     */
    public void killBackgroundProcesses(String processName) {

        String packageName = null;
        try {
            if (!processName.contains(":")) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            Log.i(TAG,"delete process success  "+packageName);
            mActivityManager.killBackgroundProcesses(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射机制 杀死进程
     * @param processName
     */
    public void killBackgroundProcessesMonth(String processName){
        try {
            ActivityManager am =(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage",String.class);
            method.invoke(am,processName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 订阅 杀死所有运行的App进程
     * @return
     */
    public Observable<Long> killAllRunningAppObserable(final boolean select){
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(killAllRunningApp(select));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 订阅 杀死指定的App进程
     * @param packageNameSets
     * @return
     */
    public Observable<Long> killListsRunningAppObservale(final List<String> packageNameSets){

        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                long memory = 0L;
                for (String string : packageNameSets) {
                    memory += killRunningApp(string);
                }
                e.onNext(memory);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 杀死指定的App 进程
     * @param packageName
     * @return
     */
    public Observable<Long> killPackageNameRunningAppObservale(final String packageName){
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(killRunningApp(packageName));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 去重类
     */
    private class  ComoaratorApp implements Comparator<AppProcessInfornBean> {
        @Override
        public int compare(AppProcessInfornBean app, AppProcessInfornBean otherapp) {
            if (app.getU_id() == otherapp.getU_id()) {
                if (app.getMemory() < otherapp.getMemory()) {
                    return 1;
                } else if (app.getMemory() == otherapp.getMemory()) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return app.getU_id() - otherapp.getU_id();
            }
        }

    }

}
