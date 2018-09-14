package com.jerrywang.phonehelper.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.screenlocker.ScreenLockerService;
import com.jerrywang.phonehelper.util.ActivityUtil;
import com.jerrywang.phonehelper.util.ToastUtil;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindColor(R.color.colorPrimary)
    int primary;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindDrawable(R.drawable.ic_menu)
    Drawable menu;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        // Set up the statusbar.
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, primary);

        // Set up the toolbar.
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        setupDrawerContent(navigationView);

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);

        if (mainFragment == null) {
            // Create the fragment
            mainFragment = MainFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.fl_content);
        }
        // Create the presenter
        new MainPresenter(mainFragment);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
//            TasksFilterType currentFiltering = (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
//            mTasksPresenter.setFiltering(currentFiltering);
        }


        //开启服务，开启锁屏界面
        startService(new Intent(MainActivity.this, ScreenLockerService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.main_update_menu_item:
                                //检查更新
                                ToastUtil.showToast(MainActivity.this, "检查更新");
                                break;
                            case R.id.main_feedback_menu_item:
                                //意见反馈
                                ToastUtil.showToast(MainActivity.this, "意见反馈");
                                break;
                            case R.id.main_comment_menu_item:
                                //评论我们
                                ToastUtil.showToast(MainActivity.this, "评论我们");
                                break;
                            case R.id.main_aboutus_menu_item:
                                //关于我们
                                ToastUtil.showToast(MainActivity.this, "关于我们");
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        //mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


}
