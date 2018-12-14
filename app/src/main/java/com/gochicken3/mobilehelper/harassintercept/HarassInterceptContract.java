package com.gochicken3.mobilehelper.harassintercept;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;

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
        void showPermissions();
    }

    public interface Presenter extends BasePresenter {


    }
}
