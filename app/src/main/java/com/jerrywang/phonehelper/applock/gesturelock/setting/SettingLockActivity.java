package com.jerrywang.phonehelper.applock.gesturelock.setting;

import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/15
 * @email 252774645@qq.com
 */
public class SettingLockActivity extends BaseActivity{


    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return SettingLockFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }
}
