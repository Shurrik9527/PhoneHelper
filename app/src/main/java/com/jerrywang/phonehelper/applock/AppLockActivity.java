package com.jerrywang.phonehelper.applock;

import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class AppLockActivity extends BaseActivity{


    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return AppLockFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle("App Lock");


    }
}
