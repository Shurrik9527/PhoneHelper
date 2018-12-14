package com.gochicken3.mobilehelper.phonebooster;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class PhoneBoosterPresenter implements PhoneBoosterContract.Presenter {
    private PhoneBoosterContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public PhoneBoosterPresenter(PhoneBoosterContract.View view) {
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
