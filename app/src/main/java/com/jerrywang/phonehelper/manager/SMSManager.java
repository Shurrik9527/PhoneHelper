package com.jerrywang.phonehelper.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jerrywang.phonehelper.bean.CallLogBean;
import com.jerrywang.phonehelper.bean.SmsBean;
import com.jerrywang.phonehelper.event.RefreshSMSEvent;
import com.jerrywang.phonehelper.util.PhoneUtils;
import com.jerrywang.phonehelper.util.RxBus.RxBus;
import com.jerrywang.phonehelper.util.SMSUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 短信记录管理
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class SMSManager {


    private static final  String TAG  =SMSManager.class.getName();
    public static SMSManager mInstance;
    private Context mContext;
    private  HarassInterceptManager manager;
    //私有构造方法
    private SMSManager(Context context){
        this.mContext =context;
        this.manager = new HarassInterceptManager(context);
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static SMSManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("SMSManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SMSManager.class) {
                if (mInstance == null) {
                    mInstance = new SMSManager(context);
                }
            }
        }
    }



    /**
     * 异步订阅手机本身通短信
     * @return
     */
    public Observable<List<SmsBean>> getMobileSMSObservable() {
        return Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
                e.onNext(SMSUtils.getSmsInfo(mContext));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取手机数据库的短信
     * @return
     */
    public Observable<List<SmsBean>> getSqliteSMSObservable() {
        return Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
                e.onNext(manager.getAllSMSInfos());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    public void updateSMSSqliteData(){
        try {
            Observable.zip(getMobileSMSObservable(),getSqliteSMSObservable(), new BiFunction<List<SmsBean>, List<SmsBean>, List<SmsBean>>() {
                @Override
                public List<SmsBean> apply(List<SmsBean> mSmsBean, List<SmsBean> mSmsBean2) throws Exception {
                    return SMSUtils.updateSMSSqlite(mSmsBean,mSmsBean2);
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SmsBean>>() {
                @Override
                public void accept(List<SmsBean> mSmsBean) throws Exception {
                    if(mSmsBean!=null&&mSmsBean.size()>0){
                        //先清理数据库
                        try {
                            if(manager!=null){
                                manager.deleteAllSMSInfo();
                                manager.instanceAllSMSInfoTable(mSmsBean);
                            }
                            Log.e(TAG,"更新短信成功...");
                            RxBus.getDefault().post(new RefreshSMSEvent());
                            return ;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.e(TAG,"更新短信记录失败或者手机本地短信没有数据");
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,"更新通讯记录失败或者手机本地通讯录没有数据");
        }

    }


}
