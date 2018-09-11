package com.jerrywang.phonehelper.screenlocker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.util.ActivityUtil;

import butterknife.ButterKnife;

public class ScreenLockerActivity extends AppCompatActivity {
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
    }
}
