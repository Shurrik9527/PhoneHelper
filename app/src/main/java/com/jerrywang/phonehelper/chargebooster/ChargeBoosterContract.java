package com.jerrywang.phonehelper.chargebooster;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

public class ChargeBoosterContract {

    public interface View extends BaseView<Presenter> {
        void showScreenLocker();
    }

    public interface Presenter extends BasePresenter {
        void startProtectCharging();

        void closeProtectCharging();

        void startChargeAlert();

        void closeChargeAlert();
    }
}
