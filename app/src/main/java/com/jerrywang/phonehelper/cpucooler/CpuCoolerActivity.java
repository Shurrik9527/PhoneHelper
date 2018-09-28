package com.jerrywang.phonehelper.cpucooler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;

import java.util.List;

public class CpuCoolerActivity extends BaseActivity {

    private  CpuCoolerFragment mCpuCoolerFragment;
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        mCpuCoolerFragment =CpuCoolerFragment.newInstance();
        return mCpuCoolerFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.cpucooler_title));

        Intent mIntent =getIntent();
        if(mIntent!=null){
            Bundle mBundle = mIntent.getBundleExtra("BUNDLE");
            if(mCpuCoolerFragment!=null){
                mCpuCoolerFragment.setArguments(mBundle);
            }
        }

    }



}
