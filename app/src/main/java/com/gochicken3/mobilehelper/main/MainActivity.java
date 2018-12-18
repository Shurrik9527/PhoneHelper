package com.gochicken3.mobilehelper.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.appsflyer.AFInAppEventType;
import com.gochicken3.mobilehelper.BaseActivity;
import com.gochicken3.mobilehelper.R;
import com.gochicken3.mobilehelper.aboutus.AboutUsActivity;
import com.gochicken3.mobilehelper.applock.AppLockActivity;
import com.gochicken3.mobilehelper.applock.gesturelock.createlock.GestureCreateActivity;
import com.gochicken3.mobilehelper.applock.gesturelock.setting.SettingLockActivity;
import com.gochicken3.mobilehelper.base.Constant;
import com.gochicken3.mobilehelper.bean.AppProcessInfornBean;
import com.gochicken3.mobilehelper.screenlocker.ScreenLockerService;
import com.gochicken3.mobilehelper.util.AdUtil;
import com.gochicken3.mobilehelper.util.AppUtil;
import com.gochicken3.mobilehelper.util.EventUtil;
import com.gochicken3.mobilehelper.util.SpHelper;
import com.gochicken3.mobilehelper.util.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;
    @BindDrawable(R.drawable.ic_menu)
    Drawable menu;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    public static List<AppProcessInfornBean> cpuLists;

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected Fragment getFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        // Create the presenter
        new MainPresenter(mainFragment);
        return mainFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void initStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, colorPrimaryDark);
        // Set up the navigation drawer.
        setupDrawerContent(navigationView);
    }

    @Override
    protected void init() {
        super.init();
        setIcon(menu);
        //开启服务，开启锁屏界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, ScreenLockerService.class));
        } else {
            startService(new Intent(this, ScreenLockerService.class));
        }

        //直接显示广告
        //AdUtil.showAds(this,"MainActivity.init()");
        //读取最新广告配置并展示
        AdUtil.getAdTypeAndShow(this, "MainActivity.init()");


//        if (Build.VERSION.SDK_INT >= 21) {
//            startJobScheduler();
//        } else {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(new Intent(getApplicationContext(), GrayService.class));
//            } else {
//                startService(new Intent(getApplicationContext(), GrayService.class));
//            }
//        }


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
//                                ToastUtil.showToast(MainActivity.this, "This is the newest version!");
                                goToAppDetailPage();
                                //有人想更新程序
                                EventUtil.sendEvent(MainActivity.this, AFInAppEventType.UPDATE, "Someone try to update!");
                                break;
                            case R.id.main_feedback_menu_item:
                                //意见反馈
//                                feedback("jerrywang724@gmail.com");
                                feedback("gochicken3@gmail.com");
                                break;
                            case R.id.main_comment_menu_item:
                                //评论我们
                                goToAppDetailPage();
                                //有人想给我们评分
                                EventUtil.sendEvent(MainActivity.this, AFInAppEventType.RATE, "Someone wants to rate us!");
                                break;
                            case R.id.main_aboutus_menu_item:
                                //关于我们
                                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.main_applock_menu_item:
                                boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
                                if (isFirstLock) {
                                    startActivity(new Intent(MainActivity.this, GestureCreateActivity.class));
                                } else {
                                    startActivity(new Intent(MainActivity.this, AppLockActivity.class));
                                }
                                break;
                            case R.id.main_setting_menu_item:
                                startActivity(new Intent(MainActivity.this, SettingLockActivity.class));
                                break;
//                            case R.id.main_harassintercept_menu_item:
//                                startActivity(new Intent(MainActivity.this, HarassInterceptActivity.class));
//                                break;

                        }
                        // Close the navigation drawer when an item is selected.
                        //mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * 意见反馈
     */
    private void feedback(String email) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + email));
        data.putExtra(Intent.EXTRA_SUBJECT, "I want to say");
        data.putExtra(Intent.EXTRA_TEXT, "Hi, There!");
        startActivity(data);
    }

    /**
     * 跳转到应用详情页面
     */
    public void goToAppDetailPage() {
        if (AppUtil.isInstalled(this, "com.android.vending")) {
            final String GOOGLE_PLAY = "com.android.vending";
            Uri uri = Uri.parse("market://details?id=com.gochicken3.mobilehelper");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_PLAY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            ToastUtil.showToast(MainActivity.this, "Please install GooglePlay first!");
        }
    }

    /**
     * 5.x以上系统启用 JobScheduler API 进行实现守护进程的唤醒操作
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void startJobScheduler() {
//        int jobId = 1;
//        JobInfo.Builder jobInfo = new JobInfo.Builder(jobId, new ComponentName(this, VMDaemonJobService.class));
//        jobInfo.setPeriodic(10000);
//        jobInfo.setPersisted(true);
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo.build());
//    }

}
