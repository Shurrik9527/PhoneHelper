package com.sharkwang8.phoneassistant.harassintercept;

import android.content.Context;

import com.sharkwang8.phoneassistant.BasePresenter;
import com.sharkwang8.phoneassistant.BaseView;
import com.sharkwang8.phoneassistant.bean.CallLogBean;

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
    }

    public interface Presenter extends BasePresenter {

        void getSystemPhoneInfor(Context context);

        List<CallLogBean> checkData(List<CallLogBean> lists);

    }
}