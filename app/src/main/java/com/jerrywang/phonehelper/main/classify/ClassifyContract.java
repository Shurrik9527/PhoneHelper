package com.jerrywang.phonehelper.main.classify;


import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class ClassifyContract {

    public interface View extends BaseView<Presenter> {
        void openClassfiyList();
    }

    public interface Presenter extends BasePresenter {
        void getClassfy();
    }
}
