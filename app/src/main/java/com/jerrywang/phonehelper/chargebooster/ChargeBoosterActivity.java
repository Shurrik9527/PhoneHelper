package com.jerrywang.phonehelper.chargebooster;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.util.ActivityUtil;
import com.jerrywang.phonehelper.util.SharedPreferencesHelper;

import butterknife.BindView;

public class ChargeBoosterActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SharedPreferencesHelper sharedPreferencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargebooster_activity);
        sharedPreferencesHelper = new SharedPreferencesHelper(ChargeBoosterActivity.this);
        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        ChargeBoosterFragment chargeBoosterFragment = (ChargeBoosterFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);

        if (chargeBoosterFragment == null) {
            chargeBoosterFragment = ChargeBoosterFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), chargeBoosterFragment, R.id.fl_content);
        }

        new ChargeBoosterPresenter(chargeBoosterFragment, sharedPreferencesHelper);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
