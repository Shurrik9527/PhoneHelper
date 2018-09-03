package com.jerrywang.phonehelper.goods.detail;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class GoodsDetailPresenter implements GoodsDetailContract.Presenter {
    private GoodsDetailContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public GoodsDetailPresenter(GoodsDetailContract.View view) {
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
