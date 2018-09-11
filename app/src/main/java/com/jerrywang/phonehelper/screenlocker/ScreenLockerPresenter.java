package com.jerrywang.phonehelper.screenlocker;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ScreenLockerPresenter implements ScreenLockerContract.Presenter {
    private ScreenLockerContract.View view;

    public ScreenLockerPresenter(ScreenLockerContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }

}
