package com.jerrywang.phonehelper.cpucooler;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class CpuCoolerContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showProcessRunning(List<AppProcessInfornBean> mlists);
        void showTemperature(float temper);
        void startScanApp();
        void initData();
        void startHeartBeat();
        void playHeartbeatAnimation();
        void cleanFinish();
        void startCleanDialog();
        void startRocketAnimation();
    }

    public interface Presenter extends BasePresenter {
        void getProcessRunningApp();
        void startScanProcessRunningApp();
        void startCleanApp(List<AppProcessInfornBean> appProcessInfornBeans);
    }
}
