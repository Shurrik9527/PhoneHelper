package com.jerrywang.phonehelper.main;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void showPhoneBooster();
        void showChargeBooster();
    }

    public interface Presenter extends BasePresenter {

    }
}
