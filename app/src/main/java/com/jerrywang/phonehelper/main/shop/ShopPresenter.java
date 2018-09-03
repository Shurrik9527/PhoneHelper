package com.jerrywang.phonehelper.main.shop;

import android.util.Log;

import com.jerrywang.phonehelper.HttpCenter;
import com.jerrywang.phonehelper.HttpResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ShopPresenter implements ShopContract.Presenter {
    private ShopContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public ShopPresenter(ShopContract.View view) {
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
    public void getData() {
        System.out.println(1);
        /*mCompositeDisposable.add(
                HttpCenter.getService()
                        .test("Shurrik")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<HttpResult<Data>>() {
                            @Override
                            public void accept(HttpResult<Data> result) throws Exception {
                                //Log.d("TestActivity", result.message);
                                taskDetailView.showMessage(result.getMessage());
                            }
                        }));*/
        view.hideRefreshing();
    }

    @Override
    public void search(String keyword) {
        mCompositeDisposable.add(
                HttpCenter.getService()
                        .search(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<HttpResult<Goods>>() {
                            @Override
                            public void accept(HttpResult<Goods> result) throws Exception {
                                Log.d("TestActivity", String.valueOf(result.getResult()));
                            }
                        }));
    }
}
