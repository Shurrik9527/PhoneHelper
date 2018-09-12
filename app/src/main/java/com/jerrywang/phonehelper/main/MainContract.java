package com.jerrywang.phonehelper.main;

import android.content.Context;
import android.os.Bundle;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

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
    }

    public interface Presenter extends BasePresenter {

        void startActivity(Context mContext,Class<?> mclass, Bundle mBundle);

    }
}
