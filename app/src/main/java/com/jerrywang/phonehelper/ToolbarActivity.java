package com.jerrywang.phonehelper;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 含有Toolbar activity
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public abstract class ToolbarActivity extends BaseActivity implements View.OnClickListener{


    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    private TextView mTitle;
    private ImageView mRightIv; // 右图标


    @Override
    protected void init() {
        super.init();
        initToolBar();
    }

    /**
     * 初始化工具栏
     * 包括标题和返回键
     */
    private void initToolBar() {
        mTitle = (TextView) findViewById(R.id.toolbar_title_tv);
        mRightIv = (ImageView) findViewById(R.id.right_iv);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null || mAppBarLayout == null) {
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 设置是否显示标题
            actionBar.setDisplayShowTitleEnabled(false);
            //默认显示返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // 设置返回键监听事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showRightIv(false);
        setListener();
    }


    /**
     * 设置标题文字
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
        super.setTitle(title);
    }

    /**
     * 设置标题栏监听
     */
    private void setListener() {
        mRightIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_iv: {
                onRightIvClick(view);
            }
            break;
        }
    }





    /**
     * 图标点击
     *
     * @param view
     */
    protected void onRightIvClick(View view) {

    }

    /**
     * 设置标题颜色
     *
     * @param textColor
     */
    @Override
    public void setTitleColor(int textColor) {
        mTitle.setTextColor(textColor);
        super.setTitleColor(textColor);
    }

    /**
     * 显示返回键
     *
     * @param show true 显示, false 不显示
     */
    protected void showBackBtn(boolean show) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        // 设置是否显示返回键
        actionBar.setDisplayHomeAsUpEnabled(show);
    }


    /**
     * 显示工具栏
     *
     * @param show true 显示, false 不显示
     */
    protected void showToolbar(boolean show) {
        if (show) {
            mAppBarLayout.setVisibility(View.VISIBLE);
            return;
        }
        mAppBarLayout.setVisibility(View.GONE);
    }

    /**
     * 设置右侧图片
     * @param resId
     */
    public void setRightIv(int resId) {
        mRightIv.setImageResource(resId);
    }



    /**
     * 设置右侧图片
     *
     * @param resId 右图图标
     * @param tag 放置标号
     */
    public void setRightIv(int resId, String tag) {
        mRightIv.setBackgroundResource(resId);
        mRightIv.setTag(tag);
    }

    /**
     * 设置右侧图片显示或隐藏
     *
     * @param show
     */
    public void showRightIv(boolean show) {
        if (show) {
            mRightIv.setVisibility(View.VISIBLE);
        } else {
            mRightIv.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题背景颜色
     * @param textColor
     */
    public void setToolBarBackGroundColor(int textColor) {
        mToolbar.setBackgroundResource(textColor);
    }

}
