package com.jerrywang.phonehelper.screenlocker;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

public class ScreenLockerContract {

    public interface View extends BaseView<Presenter> {
        void showTime(String time);
        void showChargeStatus(boolean isCharging);
        void showBatteryInfo(int percent);
    }

    public interface Presenter extends BasePresenter {
        void startTimer();
        void stopTimer();
    }
}
