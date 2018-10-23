package com.jerrywang.phonehelper.harassintercept;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.service.HarassInterceptService;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 骚扰拦截
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class HarassInterceptActivity extends BaseActivity{

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return HarassInterceptFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.harassintercept_top_title));
    }






}
