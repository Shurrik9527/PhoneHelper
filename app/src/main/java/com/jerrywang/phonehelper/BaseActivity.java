package com.jerrywang.phonehelper;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.jerrywang.phonehelper.util.ActivityUtil;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe Activity 基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity{


    /**
     * 页面间传值key常量
     */
    public static final String BUNDLE = "BUNDLE";

    /**
     * 布局文件Id
     *
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 获取fragment
     *
     * @return
     */
    protected abstract Fragment getFragment();

    /**
     * 布局中Fragment的ID
     *
     * @return
     */
    protected abstract int getFragmentContentId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initStatusBar();
        ActivityUtil.getmInstance().add(this);
        setContentView(getContentViewId());

        // 将fragment添加到activity
        addFragmentToActivity();
        // 后续初始化操作
        init();
    }

    private void addFragmentToActivity() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContentId());
        if (fragment == null) {
            // create the fragment
            fragment = getFragment();
            if (fragment != null) {
                ActivityUtil.getmInstance().addFragmentToActivity(
                        getSupportFragmentManager(),
                        fragment,
                        getFragmentContentId()
                );
            }


        }
    }


    /**
     * 加载完fragment之后进行一些初始化操作
     */
    protected void init() {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getmInstance().removeActivity(this);
    }

    /**
     * 初始化系统状态栏
     * 在4.4以上版本是否设置透明状态栏
     */
    private void initStatusBar() {
        if (!setTranslucentStatusBar()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 设置透明状态栏
     *
     * @return true 设置, false 不设置, 默认为false
     */
    protected boolean setTranslucentStatusBar() {
        return false;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    // 返回上一个画面的功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
