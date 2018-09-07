package com.jerrywang.phonehelper.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import com.jerrywang.phonehelper.util.FileUtil;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 清理管理器
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class CleanManager {

    private static final  String TAG  =CleanManager.class.getName();
    public static CleanManager mInstance;
    private Context mContext;
    //私有构造方法
    private CleanManager(Context context){
        this.mContext =context;
    }

    /**
     * 获取当前清理管理器
     * @return
     */
    public static CleanManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("CleanManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化清理管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (CleanManager.class) {
                if (mInstance == null) {
                    mInstance = new CleanManager(context);
                }
            }
        }
    }


    /**
     * 订阅 垃圾清理
     * @param junkLists
     * @return
     */
    public Observable<Boolean> cleanJunksUsingObservable(final List<String> junkLists) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(junksClean(junkLists));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 垃圾清理
     * @param junkCleanerList
     * @return
     */
    public boolean junksClean(List<String> junkCleanerList) {

        if(junkCleanerList==null)
            return false;

        for (int i = 0; i < junkCleanerList.size(); i++) {
            try {
                FileUtil.deleteTarget(junkCleanerList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     *订阅 清理App缓存
     * @param packageNameLists
     * @return
     */
    public Observable<Boolean> cleanAppsCacheObservable(final List<String> packageNameLists) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(ObservableEmitter<Boolean>subscriber) throws Exception {
                subscriber.onNext(cleanAppsCache(packageNameLists));
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 根据包名清理app下的 缓存  由于是对别的APP缓存进行处理 用到反射机制
     * @param packageNameLists
     * @return
     */
    public boolean cleanAppsCache(List<String> packageNameLists) {

        if(packageNameLists==null)
            return false;

        File externalDir = mContext.getExternalCacheDir();
        if (externalDir == null) {
            return true;
        }

        PackageManager pm = mContext.getPackageManager();
        @SuppressLint("WrongConstant") List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
        for (ApplicationInfo info : installedPackages) {
            if (packageNameLists.contains(info.packageName)) {
                String externalCacheDir = externalDir.getAbsolutePath()
                        .replace(mContext.getPackageName(), info.packageName);
                File externalCache = new File(externalCacheDir);
                if (externalCache.exists() && externalCache.isDirectory()) {
                    FileUtil.deleteTarget(externalCacheDir);
                }
            }
        }

        final boolean[] isSuccess = {false};
        try {
            Method freeStorageAndNotify = pm.getClass()
                    .getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            long freeStorageSize = Long.MAX_VALUE;
            freeStorageAndNotify.invoke(pm, freeStorageSize, new IPackageDataObserver.Stub() {

                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    isSuccess[0] = succeeded;
                }
            });

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return isSuccess[0];
    }


}
