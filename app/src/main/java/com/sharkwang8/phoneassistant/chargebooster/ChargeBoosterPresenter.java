package com.sharkwang8.phoneassistant.chargebooster;

import com.sharkwang8.phoneassistant.util.SharedPreferencesHelper;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ChargeBoosterPresenter implements ChargeBoosterContract.Presenter {
    private ChargeBoosterContract.View view;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public ChargeBoosterPresenter(ChargeBoosterContract.View view, SharedPreferencesHelper sharedPreferencesHelper) {
        this.view = view;
        this.view.setPresenter(this);
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }


    @Override
    public void loadData() {
        boolean isProtect = Boolean.parseBoolean(sharedPreferencesHelper.getSharedPreference("isProtect", false).toString().trim());
        view.switchProtectCharging(isProtect);
    }

    @Override
    public void startProtectCharging() {
        view.showScreenLocker();
        view.chargeAlert(true);
        setProtectStatus(true);
    }

    @Override
    public void closeProtectCharging() {
        view.chargeAlert(false);
        setProtectStatus(false);
    }

    @Override
    public void startChargeAlert() {
        setAlertStatus(true);
    }

    @Override
    public void closeChargeAlert() {
        setAlertStatus(false);
    }

    public void setProtectStatus(boolean isProtect) {
        sharedPreferencesHelper.put("isProtect", isProtect);
    }

    public void setAlertStatus(boolean isAlert){
        sharedPreferencesHelper.put("isAlert", isAlert);
    }

}
