package com.gochicken3.mobilehelper.screenlocker;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;

public class ScreenLockerContract {

    public interface View extends BaseView<Presenter> {
        void showTime(String time);
        void showDate(String date);
//        void showChargeStatus(boolean isCharging);
        void showBatteryInfo(int percent);
        void showNotification(String message);
    }

    public interface Presenter extends BasePresenter {
        void startTimer();
        void stopTimer();
        void checkChargingCompleted();
    }
}
