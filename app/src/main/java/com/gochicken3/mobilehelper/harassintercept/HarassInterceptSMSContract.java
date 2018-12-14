package com.gochicken3.mobilehelper.harassintercept;

import android.content.Context;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;
import com.gochicken3.mobilehelper.bean.SmsBean;

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
    }

    public interface Presenter extends BasePresenter {

        void getSystemSMSInform(Context context);

    }
}
