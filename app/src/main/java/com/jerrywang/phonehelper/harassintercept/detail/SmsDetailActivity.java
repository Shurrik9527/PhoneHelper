package com.jerrywang.phonehelper.harassintercept.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.SmsBean;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/23
 * @email 252774645@qq.com
 */
public class SmsDetailActivity extends BaseActivity{

    private  static  final String TAG =SmsDetailFragment.class.getName();
    private  SmsDetailFragment mSmsDetailFragment ;
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        mSmsDetailFragment = SmsDetailFragment.newInstance();
        Intent mIntent = getIntent();
        if (mIntent != null) {
            SmsBean temp = (SmsBean) mIntent.getSerializableExtra("BUNDLE");
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("BUNDLE", temp);
            mSmsDetailFragment.setArguments(mBundle);
        }
        return mSmsDetailFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(R.string.smsdetail_top_tile);
    }
}
