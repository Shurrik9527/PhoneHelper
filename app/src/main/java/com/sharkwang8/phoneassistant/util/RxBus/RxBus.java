package com.sharkwang8.phoneassistant.util.RxBus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 事件 组件间通信
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class RxBus {

    private final Relay<Object> mBus;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishRelay.create().toSerialized();
    }

    public static RxBus getDefault() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }


}
