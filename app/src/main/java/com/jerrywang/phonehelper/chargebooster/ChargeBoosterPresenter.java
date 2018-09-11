package com.jerrywang.phonehelper.chargebooster;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ChargeBoosterPresenter implements ChargeBoosterContract.Presenter {
    private ChargeBoosterContract.View view;
    /**
     * 充电状态 0 未充电 1 快速充电中 2 正常充电 3 涓流充电 4 充电完成
     */
    private int chargeStatus;

    public ChargeBoosterPresenter(ChargeBoosterContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }


    @Override
    public void startProtectCharging() {
        if(isCharging()) {
            view.showScreenLocker();
        }
    }

    @Override
    public void closeProtectCharging() {
        setChargeStatus(0);
    }

    @Override
    public void startChargeAlert() {
        view.showMessageTips("开启充电提醒");
    }

    @Override
    public void closeChargeAlert() {
        view.showMessageTips("关闭充电提醒");
    }

    /**
     * 是否正在充电
     */
    public boolean isCharging() {
        return true;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }
}
