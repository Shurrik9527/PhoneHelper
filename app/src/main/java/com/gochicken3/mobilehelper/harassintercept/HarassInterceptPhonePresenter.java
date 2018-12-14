package com.gochicken3.mobilehelper.harassintercept;

import android.content.Context;
import android.text.TextUtils;

import com.gochicken3.mobilehelper.bean.CallLogBean;
import com.gochicken3.mobilehelper.manager.HarassInterceptManager;

import java.util.ArrayList;
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
public class HarassInterceptPhonePresenter implements HarassInterceptPhoneContract.Presenter{

    private static final String TAG =HarassInterceptPhonePresenter.class.getName();
    private Context mContext;
    private HarassInterceptPhoneContract.View mView=null;
    private HarassInterceptManager manager;

    public HarassInterceptPhonePresenter(final HarassInterceptPhoneContract.View view, Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
        this.manager =new HarassInterceptManager(context);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getSystemPhoneInfor(final Context context) {
        try {
            Observable.create(new ObservableOnSubscribe<List<CallLogBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<CallLogBean>> e) throws Exception {
                    e.onNext(manager.getAllPhoneInfos());
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CallLogBean>>() {
                @Override
                public void accept(List<CallLogBean> smsBeans) throws Exception {
                    if(smsBeans!=null){
                        mView.showData(smsBeans);
                    }
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public List<CallLogBean> checkData(List<CallLogBean> mlists) {

        List<CallLogBean> tempList = new ArrayList<>();
        List<CallLogBean> lists = mlists;
        for (int i = 0; i < lists.size() - 1; i++) {
            CallLogBean mCallLogBean =lists.get(i);
            int size =0;
            for (int j = lists.size() - 1; j > i; j--)
            {
                CallLogBean mCallLogBeanj =lists.get(j);
                if (!TextUtils.isEmpty(mCallLogBean.getPhoneNum())&!TextUtils.isEmpty(mCallLogBeanj.getPhoneNum())) {
                    if(mCallLogBean.getPhoneNum().equals(mCallLogBeanj.getPhoneNum())){
                        tempList.add(mCallLogBeanj);
                        size++;
                        lists.remove(j);//删除重复元素
                    }
                }
            }
        }
        return lists;
    }

}
