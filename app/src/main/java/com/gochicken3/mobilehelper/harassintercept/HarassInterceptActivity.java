package com.gochicken3.mobilehelper.harassintercept;

import android.support.v4.app.Fragment;

import com.gochicken3.mobilehelper.BaseActivity;
import com.gochicken3.mobilehelper.R;


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
