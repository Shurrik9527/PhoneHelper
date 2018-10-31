package com.jerrywang.phonehelper.trafficstatistics;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.jerrywang.phonehelper.applock.AppLockPresenter;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;
import com.jerrywang.phonehelper.util.TrafficStatisticsUtil;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisticsPresenter implements TrafficStatisticsContract.Presenter{


    private static final String TAG =AppLockPresenter.class.getName();
    private Context mContext;
    private TrafficStatisticsContract.View mView=null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public TrafficStatisticsPresenter(final TrafficStatisticsContract.View view, Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
    }




    @Override
    public void getTrafficStatistics() {

        Observable.create(new ObservableOnSubscribe<List<TrafficStatisticsBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<TrafficStatisticsBean>> e) throws Exception {

                List<TrafficStatisticsBean> mlist =TrafficStatisticsUtil.getAppTrafficStatistics(mContext);
                if(mlist!=null){
                    e.onNext(mlist);
                }else{
                    e.onError(new Throwable("List<TrafficStatisticsBean> is null"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<TrafficStatisticsBean>>() {
            @Override
            public void accept(List<TrafficStatisticsBean> trafficStatisticsBeans) throws Exception {
                if(mView!=null){
                    mView.showData(trafficStatisticsBeans);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void getTotalSize() {

        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
               e.onNext(TrafficStatisticsUtil.getTotalTrafficStatistics(mContext));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if(mView!=null){
                    mView.showAllTraffic(aLong);
                }
            }
        });

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
