package com.jerrywang.phonehelper.junkcleaner;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class JunkCleanerPresenter implements JunkCleanerContract.Presenter {
    private JunkCleanerContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public JunkCleanerPresenter(JunkCleanerContract.View view) {
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
