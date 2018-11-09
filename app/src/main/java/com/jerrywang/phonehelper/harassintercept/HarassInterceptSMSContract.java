package com.jerrywang.phonehelper.harassintercept;

import android.content.Context;

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
public interface HarassInterceptSMSContract {

    public interface View extends BaseView<HarassInterceptSMSContract.Presenter> {
        void initData();
        void initRecycleView();
        void showData(List<SmsBean> mlist);
        void refreshData();
        void showPermissions();
        void updataPhone();
    }

    public interface Presenter extends BasePresenter {

        void getSystemSMSInform(Context context);

    }
}
