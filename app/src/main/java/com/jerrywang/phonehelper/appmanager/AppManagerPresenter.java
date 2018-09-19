package com.jerrywang.phonehelper.appmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.jerrywang.phonehelper.event.UninstallEvent;
import com.jerrywang.phonehelper.util.RxBus.RxBusHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class AppManagerPresenter implements AppManagerContract.Presenter {
    private AppManagerContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public AppManagerPresenter(AppManagerContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        //处理卸载完成事件
        RxBusHelper.doOnMainThread(UninstallEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<UninstallEvent>() {
            @Override
            public void onEvent(UninstallEvent junkCleanerTotalSizeEvent) {
                if (mView != null) {
                    mView.refresh();
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if(mCompositeDisposable.isDisposed()){
            mCompositeDisposable.clear();
        }
    }

    @Override
    public List<String> loadData(Context context) {
        List<String> data = new ArrayList<String>();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));// 字典排序
        for (ApplicationInfo app : listAppcations) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                //非系统程序
                data.add(app.packageName);
            } else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                data.add(app.packageName);
            }
        }
        return data;
    }
}
