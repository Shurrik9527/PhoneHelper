package com.jerrywang.phonehelper.junkcleaner;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class JunkCleanerContract {

    public interface View extends BaseView<Presenter> {
        //初始化View
        void initView();
        //初始数据
        void initData();


    }

    public interface Presenter extends BasePresenter {

    }
}
