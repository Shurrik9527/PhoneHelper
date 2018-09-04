package com.jerrywang.phonehelper.cpucooler;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class CpuCoolerPresenter implements CpuCoolerContract.Presenter {
    private CpuCoolerContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public CpuCoolerPresenter(CpuCoolerContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

}
