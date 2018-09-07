package com.jerrywang.phonehelper.junkcleaner;

import android.content.Context;

import com.jerrywang.phonehelper.App;
import com.jerrywang.phonehelper.manager.CleanManager;
import com.jerrywang.phonehelper.manager.JunkCleanerManager;
import com.jerrywang.phonehelper.manager.ProcessManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 Presenter
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerPresenter implements JunkCleanerContract.Presenter {

    private static final String TAG =JunkCleanerPresenter.class.getName();
    private JunkCleanerContract.View view;
    private CompositeDisposable mCompositeDisposable;
    private Context mContext;
    private JunkCleanerManager mJunkCleanerManager;//垃圾清理管理
    private CleanManager mCleanManager;//清理管理
    private ProcessManager mProcessManager;//进程管理


    public JunkCleanerPresenter(JunkCleanerContract.View view) {
        this.view = view;
        this.mContext = App.getmContext();
        this.view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mJunkCleanerManager =JunkCleanerManager.getInstance();
        mCleanManager =CleanManager.getmInstance();
        mProcessManager =ProcessManager.getInstance();




    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

}
