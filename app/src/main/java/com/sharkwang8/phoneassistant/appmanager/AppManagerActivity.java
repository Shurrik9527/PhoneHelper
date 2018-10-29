package com.sharkwang8.phoneassistant.appmanager;

import android.support.v4.app.Fragment;

import com.sharkwang8.phoneassistant.BaseActivity;
import com.sharkwang8.phoneassistant.R;

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
