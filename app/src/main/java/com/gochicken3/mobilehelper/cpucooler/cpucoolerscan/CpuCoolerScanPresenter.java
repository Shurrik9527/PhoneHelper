package com.gochicken3.mobilehelper.cpucooler.cpucoolerscan;

import com.gochicken3.mobilehelper.bean.AppProcessInfornBean;
import com.gochicken3.mobilehelper.manager.ProcessManager;
import com.gochicken3.mobilehelper.util.CpuUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class CpuCoolerScanPresenter implements CpuCoolerScanContract.Presenter {
    private CpuCoolerScanContract.View view;


    public CpuCoolerScanPresenter(CpuCoolerScanContract.View view) {
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getProcessRunningApp() {

        ProcessManager.getInstance().getRunningAppListObservable(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppProcessInfornBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List<AppProcessInfornBean> appProcessInfornBeans) {
                       view.showProcessRunning(appProcessInfornBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void startScanProcessRunningApp() {

        Observable.create(new ObservableOnSubscribe<Float>() {
            @Override
            public void subscribe(ObservableEmitter<Float> e) throws Exception {
                e.onNext(CpuUtil.cpuAverageTemperature());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) throws Exception {
                if(view!=null){
                    view.showTemperature(aFloat);
                }
            }
        });

    }

}
