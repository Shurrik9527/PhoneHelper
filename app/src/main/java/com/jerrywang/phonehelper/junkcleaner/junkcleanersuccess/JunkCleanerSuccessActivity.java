package com.jerrywang.phonehelper.junkcleaner.junkcleanersuccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.ToolbarActivity;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class JunkCleanerSuccessActivity extends ToolbarActivity{

    private static  final String TAG = JunkCleanerSuccessActivity.class.getName();
    private JunkCleanerSuccessFragment junkCleanerSuccessFragment= null;
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        junkCleanerSuccessFragment =JunkCleanerSuccessFragment.newInstance();
        Intent mIntent =getIntent();
        if(mIntent!=null){
            String temp = mIntent.getStringExtra("BUNDLE");
            Bundle mBundle = new Bundle();
            mBundle.putString("BUNDLE",temp);
            junkCleanerSuccessFragment.setArguments(mBundle);
        }
        return junkCleanerSuccessFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.contentFragment;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.junkcleaner_title));
    }
}
