package com.jerrywang.phonehelper.appmanager;

import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;

public class AppManagerActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        AppManagerFragment appManagerFragment = AppManagerFragment.newInstance();
        // Create the presenter
        new AppManagerPresenter(appManagerFragment);
        return appManagerFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.appmanager_title));
    }
}
