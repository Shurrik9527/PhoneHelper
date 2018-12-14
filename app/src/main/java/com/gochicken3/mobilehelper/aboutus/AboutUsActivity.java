package com.gochicken3.mobilehelper.aboutus;

import android.support.v4.app.Fragment;

import com.gochicken3.mobilehelper.BaseActivity;
import com.gochicken3.mobilehelper.R;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        AboutUsFragment chargeBoosterFragment = AboutUsFragment.newInstance();
        // Create the presenter
        new AboutUsPresenter(chargeBoosterFragment);
        return chargeBoosterFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.aboutus_title));
    }
}
