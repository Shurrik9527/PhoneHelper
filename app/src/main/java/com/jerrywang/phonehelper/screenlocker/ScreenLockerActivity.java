package com.jerrywang.phonehelper.screenlocker;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;

import butterknife.BindColor;

public class ScreenLockerActivity extends BaseActivity {
    private BatteryReceiver receiver;
//    @BindView(R.id.fl_content)
//    FrameLayout flContent;
    @BindColor(R.color.bg_start)
    int bg_start;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        ScreenLockerFragment chargeBoosterFragment =  ScreenLockerFragment.newInstance();
        // Create the presenter
        new ScreenLockerPresenter(chargeBoosterFragment);
        receiver = new BatteryReceiver(chargeBoosterFragment);
        return chargeBoosterFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    protected void initStatusBar() {
        //透明显示状态栏
        StatusBarUtil.setColor(this, bg_start);
    }

    @Override
    protected void init() {
        super.init();
        //隐藏导航栏
        hideToolbar();
        //获取桌面壁纸并设置为默认屏保
//        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//        flContent.setBackground(mWallpaperManager.getDrawable());
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
