package com.jerrywang.phonehelper.util.RxBus;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private final Subject<Object> bus;
    private Map<Object, List<Subscription>> subscriptions;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getDefault() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送事件
     *
     * @param event 事件对象
     */
    public void post(Object event) {
        if (bus.hasObservers()) {
            bus.onNext(event);
        }
    }

    /**
     * 订阅事件
     *
     * @param eventType 事件对象
     * @param <T>       事件类型
     * @return 特定类型的Observable
     */
    public <T> Observable<T> observe(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 添加订阅协议
     *
     * @param subscriber   订阅者
     * @param subscription 订阅协议
     */
    public synchronized void add(Object subscriber, Subscription subscription) {
        if (subscriptions == null) {
            subscriptions = new HashMap<>();
        }

        List<Subscription> list = subscriptions.get(subscriber);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(subscription);
        subscriptions.put(subscriber, list);
    }

    /**
     * 取消事件订阅
     *
     * @param subscriber 订阅者
     */
    public synchronized void unsubscribe(Object subscriber) {
        if (subscriptions == null) return;

        List<Subscription> list = subscriptions.get(subscriber);
        if (list == null || list.isEmpty()) return;

        for (Subscription subscription : list) {
            subscription.cancel();
        }
        list.clear();
        subscriptions.remove(subscriber);
    }

    /**
     * 取消所有订阅
     */
    public void unsubscribeAll() {
        Set<Map.Entry<Object, List<Subscription>>> set = subscriptions.entrySet();
        for (Map.Entry<Object, List<Subscription>> entry : set) {
            List<Subscription> list = entry.getValue();
            for (Subscription subscription : list) {
                subscription.cancel();
            }
            list.clear();
        }
        subscriptions = null;
    }

    private static class SingletonHolder {
        public static volatile RxBus INSTANCE = new RxBus();
    }

    public <T> Observable<T> toObservable (Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> Observable<T> IoToUiObservable(Class<T> eventType){
        return bus.ofType(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
