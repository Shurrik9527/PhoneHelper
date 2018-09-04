package com.jerrywang.phonehelper.settings;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public SettingsPresenter(SettingsContract.View view) {
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
