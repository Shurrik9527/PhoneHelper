package com.jerrywang.phonehelper.screenlocker;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.util.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenLockerActivity extends AppCompatActivity {
    private BatteryReceiver receiver;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenlocker_activity);
        ButterKnife.bind(this);

        ScreenLockerFragment chargeBoosterFragment = (ScreenLockerFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);

        if (chargeBoosterFragment == null) {
            chargeBoosterFragment = ScreenLockerFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), chargeBoosterFragment, R.id.fl_content);
        }

        new ScreenLockerPresenter(chargeBoosterFragment);

        receiver = new BatteryReceiver(chargeBoosterFragment);

        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        flContent.setBackground(mWallpaperManager.getDrawable());
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
