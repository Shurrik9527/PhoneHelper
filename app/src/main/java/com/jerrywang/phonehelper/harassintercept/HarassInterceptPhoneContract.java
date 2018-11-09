package com.jerrywang.phonehelper.harassintercept;

import android.content.Context;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;
import com.jerrywang.phonehelper.bean.CallLogBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public interface HarassInterceptPhoneContract {

    public interface View extends BaseView<HarassInterceptPhoneContract.Presenter> {
        void initData();
        void initRecycleView();
        void showData(List<CallLogBean> lists);
        void refreshData();
        void callPhone(String phone);
        void showPermissions();
        void updataPhone();
    }

    public interface Presenter extends BasePresenter {

        void getSystemPhoneInfor(Context context);

        List<CallLogBean> checkData(List<CallLogBean> lists);

    }
}
