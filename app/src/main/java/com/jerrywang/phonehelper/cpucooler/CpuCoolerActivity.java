package com.jerrywang.phonehelper.cpucooler;

import android.support.v4.app.Fragment;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.ToolbarActivity;

public class CpuCoolerActivity extends ToolbarActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return CpuCoolerFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.contentFragment;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.cpucooler_title));
    }

}
