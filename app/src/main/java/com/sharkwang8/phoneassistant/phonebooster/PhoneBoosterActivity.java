package com.sharkwang8.phoneassistant.phonebooster;

import android.support.v4.app.Fragment;

import com.sharkwang8.phoneassistant.BaseActivity;
import com.sharkwang8.phoneassistant.R;

public class PhoneBoosterActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        PhoneBoosterFragment phoneBoosterFragment = PhoneBoosterFragment.newInstance();
        // Create the presenter
        new PhoneBoosterPresenter(phoneBoosterFragment);
        return phoneBoosterFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.phonebooster_title));
    }

}
