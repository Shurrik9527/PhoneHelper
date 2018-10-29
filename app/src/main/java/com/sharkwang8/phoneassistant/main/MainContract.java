package com.sharkwang8.phoneassistant.main;

import android.content.Context;
import android.os.Bundle;

import com.sharkwang8.phoneassistant.BasePresenter;
import com.sharkwang8.phoneassistant.BaseView;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void showPhoneBooster();
        void showChargeBooster();
        void showPermissions();
    }

    public interface Presenter extends BasePresenter {

        void startActivity(Context mContext,Class<?> mclass, Bundle mBundle);

    }
}
