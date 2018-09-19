package com.jerrywang.phonehelper.junkcleaner.optimized;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.ToolbarActivity;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 最佳状态
 * @date 2018/9/18
 * @email 252774645@qq.com
 */
public class OptimizedActivity extends ToolbarActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return OptimizedFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.contentFragment;
    }


    @Override
    protected void init() {
        super.init();
        Intent mIntent =getIntent();
        String mTitle =null;
        if(mIntent!=null){
            mTitle = mIntent.getStringExtra(BUNDLE);
        }
        if(!TextUtils.isEmpty(mTitle)){
            setTitle(mTitle+"");
        }
    }
}
