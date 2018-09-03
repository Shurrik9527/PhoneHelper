package com.jerrywang.phonehelper.main.shop;


import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * Created by Shurrik on 2017/11/24.
 */

public class ShopContract {

    public interface View extends BaseView<Presenter> {
        void hideRefreshing();
    }

    public interface Presenter extends BasePresenter {
        void getData();
        void search(String key);
    }
}
