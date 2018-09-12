package com.jerrywang.phonehelper.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public MainPresenter(MainContract.View view) {
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
    public void startActivity(Context mContext, Class<?> mclass, Bundle mBundle) {
        if(mclass==null||mContext==null)
            return;
        Intent mIntent = new Intent(mContext,mclass);
        if(mBundle!=null){
            mIntent.putExtra("BUNDLE",mBundle);
        }
        mContext.startActivity(mIntent);
    }
}
