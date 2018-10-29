package com.sharkwang8.phoneassistant.aboutus;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class AboutUsPresenter implements AboutUsContract.Presenter {
    private AboutUsContract.View view;

    public AboutUsPresenter(AboutUsContract.View view) {
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
