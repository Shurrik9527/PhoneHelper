package com.jerrywang.phonehelper.trafficstatistics;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisticsActivity extends BaseActivity{

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return TrafficStatisticsFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle("TrafficStatistics");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.trafficstatistics_setting_menu,menu);
//        return true;
//    }





}
