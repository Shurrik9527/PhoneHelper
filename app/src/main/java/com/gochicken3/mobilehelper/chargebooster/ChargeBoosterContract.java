package com.gochicken3.mobilehelper.chargebooster;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;

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
