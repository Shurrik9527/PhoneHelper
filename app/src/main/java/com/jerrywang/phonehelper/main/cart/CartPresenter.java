package com.jerrywang.phonehelper.main.cart;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public CartPresenter(CartContract.View view) {
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
