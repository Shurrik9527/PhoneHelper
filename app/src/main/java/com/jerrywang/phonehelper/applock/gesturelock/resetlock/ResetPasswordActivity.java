package com.jerrywang.phonehelper.applock.gesturelock.resetlock;

import android.support.v4.app.Fragment;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.applock.gesturelock.createlock.GestureCreateFragment;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 重新设置密码 界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class ResetPasswordActivity extends BaseActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return ResetPasswordFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(R.string.resetpassword_top_title);
    }
}
