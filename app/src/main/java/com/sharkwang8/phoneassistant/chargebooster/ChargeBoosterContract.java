package com.sharkwang8.phoneassistant.chargebooster;

import com.sharkwang8.phoneassistant.BasePresenter;
import com.sharkwang8.phoneassistant.BaseView;

public class ChargeBoosterContract {

    public interface View extends BaseView<Presenter> {
        void showScreenLocker();
        void switchProtectCharging(boolean checked);
        void chargeAlert(boolean enable);
    }

    public interface Presenter extends BasePresenter {
        void loadData();

        void startProtectCharging();

        void closeProtectCharging();

        void startChargeAlert();

        void closeChargeAlert();
    }
}
