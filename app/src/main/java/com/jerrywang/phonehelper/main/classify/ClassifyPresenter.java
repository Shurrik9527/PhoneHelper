package com.jerrywang.phonehelper.main.classify;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ClassifyPresenter implements ClassifyContract.Presenter {
    private ClassifyContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public ClassifyPresenter(ClassifyContract.View view) {
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


    @Override
    public void getClassfy() {

    }
}
