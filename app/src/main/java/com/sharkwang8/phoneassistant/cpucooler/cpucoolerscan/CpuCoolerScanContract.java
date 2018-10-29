package com.sharkwang8.phoneassistant.cpucooler.cpucoolerscan;

import com.sharkwang8.phoneassistant.BasePresenter;
import com.sharkwang8.phoneassistant.BaseView;
import com.sharkwang8.phoneassistant.bean.AppProcessInfornBean;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class CpuCoolerScanContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showProcessRunning(List<AppProcessInfornBean> mlists);
        void showTemperature(float temper);
        void initData();

        //判断是否是最佳
        boolean isOptimized();
    }

    public interface Presenter extends BasePresenter {
        void getProcessRunningApp();
        void startScanProcessRunningApp();
    }
}
