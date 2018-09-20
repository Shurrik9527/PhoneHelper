package com.jerrywang.phonehelper.appmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class AppManagerContract {

    public interface View extends BaseView<Presenter> {
        void refresh();
    }

    public interface Presenter extends BasePresenter {
        List<ApplicationInfo> loadData(Context context);
    }
}
