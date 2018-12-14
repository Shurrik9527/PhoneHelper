package com.gochicken3.mobilehelper.harassintercept;

import android.content.Context;

import com.gochicken3.mobilehelper.bean.SmsBean;
import com.gochicken3.mobilehelper.manager.HarassInterceptManager;

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
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class HarassInterceptSMSPresenter implements HarassInterceptSMSContract.Presenter{

    private static final String TAG =HarassInterceptSMSPresenter.class.getName();
    private Context mContext;
    private HarassInterceptSMSContract.View mView=null;
    private HarassInterceptManager manager;

    public HarassInterceptSMSPresenter(final HarassInterceptSMSContract.View view, Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
        this.manager = new HarassInterceptManager(mContext);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void getSystemSMSInform(final Context context) {

//
//        boolean isfirst = (boolean) SpHelper.getInstance().get(Constant.UPDATE_SMS_ISFIRST_SQLITE,false);
//        if(!isfirst){
//            try{
//                Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
//                        e.onNext(SMSUtils.getSmsInfo(context));
//                    }
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SmsBean>>() {
//                    @Override
//                    public void accept(List<SmsBean> smsBeans) throws Exception {
//                        if(smsBeans!=null){
//                            mView.showData(smsBeans);
//                            SMSManager.getmInstance().updateSMSSqliteData();
//                            SpHelper.getInstance().put(Constant.UPDATE_SMS_ISFIRST_SQLITE,true);
//                        }
//                    }
//                });
//            }catch (Exception e){
//
//            }
//        }else{
            try{
                Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
                        e.onNext(manager.getAllSMSInfos());
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SmsBean>>() {
                    @Override
                    public void accept(List<SmsBean> smsBeans) throws Exception {
                        if(smsBeans!=null){
                            mView.showData(smsBeans);
                        }
                    }
                });
            }catch (Exception e){

            }
//        }
//

    }
}
