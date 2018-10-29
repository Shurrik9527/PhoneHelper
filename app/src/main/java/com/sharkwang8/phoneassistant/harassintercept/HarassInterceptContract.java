package com.sharkwang8.phoneassistant.harassintercept;

import com.sharkwang8.phoneassistant.BasePresenter;
import com.sharkwang8.phoneassistant.BaseView;
import com.sharkwang8.phoneassistant.bean.SmsBean;

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
