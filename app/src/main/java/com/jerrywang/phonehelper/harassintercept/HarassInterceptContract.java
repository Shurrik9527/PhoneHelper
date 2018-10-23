package com.jerrywang.phonehelper.harassintercept;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;
import com.jerrywang.phonehelper.bean.SmsBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public interface HarassInterceptContract {

    public interface View extends BaseView<HarassInterceptContract.Presenter> {
        void initData();
        void initViewPager();
        void initRecycleView();
        void initImageView();
    }

    public interface Presenter extends BasePresenter {


    }
}
