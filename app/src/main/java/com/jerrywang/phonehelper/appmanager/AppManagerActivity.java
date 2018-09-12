package com.jerrywang.phonehelper.appmanager;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.util.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppManagerActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appmanager_activity);
        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        AppManagerFragment appManagerFragment = (AppManagerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_content);

        if (appManagerFragment == null) {
            appManagerFragment = AppManagerFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), appManagerFragment, R.id.fl_content);
        }

        new AppManagerPresenter(appManagerFragment);
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
