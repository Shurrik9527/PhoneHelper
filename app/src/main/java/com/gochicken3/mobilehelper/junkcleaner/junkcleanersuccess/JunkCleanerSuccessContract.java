package com.gochicken3.mobilehelper.junkcleaner.junkcleanersuccess;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class JunkCleanerSuccessContract {

    public interface View extends BaseView<Presenter> {
        void startScan();
        void startRotateAnimation();
        //总共垃圾数目
        void showTotalSize(String size);
    }

    public interface Presenter extends BasePresenter {
        void startScanTask();
    }
}
