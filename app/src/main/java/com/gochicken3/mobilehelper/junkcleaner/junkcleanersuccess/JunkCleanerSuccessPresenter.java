package com.gochicken3.mobilehelper.junkcleaner.junkcleanersuccess;

import com.gochicken3.mobilehelper.bean.AppProcessInfornBean;
import com.gochicken3.mobilehelper.bean.JunkCleanerGroupBean;
import com.gochicken3.mobilehelper.bean.JunkCleanerInformBean;
import com.gochicken3.mobilehelper.bean.JunkCleanerTypeBean;
import com.gochicken3.mobilehelper.event.JunkCleanerCurrentSizeEvent;
import com.gochicken3.mobilehelper.event.JunkCleanerItemTotalSizeEvent;
import com.gochicken3.mobilehelper.event.JunkCleanerShowDialogEvent;
import com.gochicken3.mobilehelper.event.JunkCleanerTotalSizeEvent;
import com.gochicken3.mobilehelper.manager.JunkCleanerManager;
import com.gochicken3.mobilehelper.util.FormatUtil;
import com.gochicken3.mobilehelper.util.RxBus.RxBus;
import com.gochicken3.mobilehelper.util.RxBus.RxBusHelper;
import java.util.ArrayList;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class JunkCleanerSuccessPresenter implements JunkCleanerSuccessContract.Presenter {

    private static final String TAG =JunkCleanerSuccessPresenter.class.getName();
    private JunkCleanerSuccessContract.View view;
    private CompositeDisposable mCompositeDisposable;
    private JunkCleanerManager mJunkCleanerManager;//垃圾清理管理
    private long mTotalJunkSize;

    public JunkCleanerSuccessPresenter(final JunkCleanerSuccessContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        mJunkCleanerManager =JunkCleanerManager.getInstance();
        mCompositeDisposable = new CompositeDisposable();
        //总垃圾数
        RxBusHelper.doOnMainThread(JunkCleanerTotalSizeEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerTotalSizeEvent>() {
            @Override
            public void onEvent(JunkCleanerTotalSizeEvent junkCleanerTotalSizeEvent) {
                if(view!=null){
                    view.showTotalSize(junkCleanerTotalSizeEvent.getTotalSize());
                }
            }
        });


//        //某项垃圾大小
//        RxBusHelper.doOnMainThread(JunkCleanerItemTotalSizeEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerItemTotalSizeEvent>() {
//            @Override
//            public void onEvent(JunkCleanerItemTotalSizeEvent junkCleanerItemTotalSizeEvent) {
//                if(view!=null){
//                    view.showItemTotalJunkSize(junkCleanerItemTotalSizeEvent.getIndex(), junkCleanerItemTotalSizeEvent.getTotalSize());
//                }
//            }
//
//        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void startScanTask() {

        mJunkCleanerManager.startScanTask();
        mJunkCleanerManager.setScanListener(new JunkCleanerManager.IScanListener() {

            @Override
            public void startScan() {
                RxBus.getDefault().post(new JunkCleanerShowDialogEvent(0));
                mTotalJunkSize = 0L;
            }
            //扫描结束
            @Override
            public void isOverScanFinish(ArrayList<JunkCleanerInformBean> apkList, ArrayList<JunkCleanerInformBean> logList, ArrayList<JunkCleanerInformBean> tempList) {
            }
            //系统缓存 扫描结束
            @Override
            public void isSysCacheScanFinish(ArrayList<JunkCleanerInformBean> sysCacheList) {
                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.CACHE, getFilterJunkSize(sysCacheList)));
            }

            //进程扫描结束
            @Override
            public void isProcessScanFinish(ArrayList<AppProcessInfornBean> processList) {
                long size = 0L;
                for (AppProcessInfornBean info : processList) {
                    size += info.getMemory();
                }
                mTotalJunkSize += size;
                //刷新总数
                RxBus.getDefault().post(new JunkCleanerTotalSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
            }

            //扫描都结束
            @Override
            public void isAllScanFinish(JunkCleanerGroupBean junkCleanerGroupBean) {
                RxBus.getDefault().post(new JunkCleanerCurrentSizeEvent(mTotalJunkSize));
            }
            //当前正在扫描
            @Override
            public void currentOverScanJunk(JunkCleanerInformBean junkCleanerInformBean) {
            }
            //扫描当前系统缓存
            @Override
            public void currentSysCacheScanJunk(JunkCleanerInformBean junkCleanerInformBean) {

            }
        });
    }


    /**
     * 计算总垃圾数
     * @param list
     * @return
     */
    private String getFilterJunkSize(ArrayList<JunkCleanerInformBean> list) {

        long size = 0L;
        for (JunkCleanerInformBean info : list) {
            size += info.getmSize();
        }
        return FormatUtil.formatFileSize(size).toString();
    }


}
