package com.jerrywang.phonehelper.junkcleaner.junkcleanersuccess;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

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
