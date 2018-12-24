package com.sharkwang8.phoneassistant.harassintercept;

import android.content.Context;
import android.content.pm.PackageManager;

import com.sharkwang8.phoneassistant.applock.AppLockContract;
import com.sharkwang8.phoneassistant.applock.AppLockPresenter;
import com.sharkwang8.phoneassistant.manager.CommLockInfoManager;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class HarassInterceptPresenter implements HarassInterceptContract.Presenter{

    private static final String TAG =HarassInterceptPresenter.class.getName();
    private Context mContext;
    private HarassInterceptContract.View mView=null;

    public HarassInterceptPresenter(final HarassInterceptContract.View view,Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}