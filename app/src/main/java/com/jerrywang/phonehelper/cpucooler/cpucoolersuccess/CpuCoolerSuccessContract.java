package com.jerrywang.phonehelper.cpucooler.cpucoolersuccess;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CpuCoolerSuccessContract {

    public interface View extends BaseView<Presenter> {
        void showTemperature(float temper);
        void startScan();
        void startRotateAnimation();
        void triggerCountDown();
    }

    public interface Presenter extends BasePresenter {
        void startScanProcess();
    }
}
