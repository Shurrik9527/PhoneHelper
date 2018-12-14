package com.sharkwang8.phoneassistant.appmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.sharkwang8.phoneassistant.bean.AppInformBean;
import com.sharkwang8.phoneassistant.event.UninstallEvent;
import com.sharkwang8.phoneassistant.util.AppUtil;
import com.sharkwang8.phoneassistant.util.RxBus.RxBusHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void loadData(final Context context) {

        if(context==null){
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<ApplicationInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ApplicationInfo>> e) throws Exception {
                e.onNext(AppUtil.getUnInstalledApplicationInfo(context));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ApplicationInfo>>() {
            @Override
            public void accept(List<ApplicationInfo> applicationInfos) throws Exception {
                if(mView!=null&&applicationInfos!=null&&applicationInfos.size()>0){
                    mView.showAppData(applicationInfos);
                }
            }
        });

    }
}
