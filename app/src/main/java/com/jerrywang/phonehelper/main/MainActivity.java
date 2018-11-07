package com.jerrywang.phonehelper.main;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.GrayService;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.VMDaemonJobService;
import com.jerrywang.phonehelper.aboutus.AboutUsActivity;
import com.jerrywang.phonehelper.applock.AppLockActivity;
import com.jerrywang.phonehelper.applock.gesturelock.createlock.GestureCreateActivity;
import com.jerrywang.phonehelper.applock.gesturelock.setting.SettingLockActivity;
import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;
import com.jerrywang.phonehelper.harassintercept.HarassInterceptActivity;
import com.jerrywang.phonehelper.manager.AddressListManager;
import com.jerrywang.phonehelper.manager.CallLogManager;
import com.jerrywang.phonehelper.manager.SMSManager;
import com.jerrywang.phonehelper.screenlocker.ScreenLockerService;
import com.jerrywang.phonehelper.trafficstatistics.TrafficStatisticsActivity;
import com.jerrywang.phonehelper.util.SpHelper;
import com.jerrywang.phonehelper.util.ToastUtil;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;
    @BindDrawable(R.drawable.ic_menu)
    Drawable menu;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
//    private InterstitialAd interstitialAd;


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
        startService(new Intent(MainActivity.this, ScreenLockerService.class));

        //初始化AdMob
//        MobileAds.initialize(this);
//        //初始化Interstitial Ads
//        interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId("ca-app-pub-7217354661273867/8093273638");
//        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("3354EE0DE60D4DE6C845A1C28842FDEA")
//                .build();
//        interstitialAd.loadAd(request);
//        //初始化成功以后直接显示
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                interstitialAd.show();
//            }
//        });


        if (Build.VERSION.SDK_INT >= 21) {
            startJobScheduler();
        } else {
            startService(new Intent(getApplicationContext(), GrayService.class));
        }

        //隐藏App Icon
       hideAppIcon();

    }


    private void hideAppIcon() {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(1000*60*60*12);//12小时后桌面图标影藏
                PackageManager pm=getPackageManager();
                pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);//影藏图标
            }
        }.start();
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
                                ToastUtil.showToast(MainActivity.this, "This is the newest version!");
//                                goToAppDetailPage();
                                break;
                            case R.id.main_feedback_menu_item:
                                //意见反馈
                                //ToastUtil.showToast(MainActivity.this, "意见反馈");
//                                feedback("jerrywang724@gmail.com");
                                feedback("sharkwang8@gmail.com");
                                break;
//                            case R.id.main_comment_menu_item:
                                //评论我们
//                                ToastUtil.showToast(MainActivity.this, "评论我们");
//                                goToAppDetailPage();
//                                break;
                            case R.id.main_aboutus_menu_item:
                                //关于我们
                                //ToastUtil.showToast(MainActivity.this, "关于我们");
                                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.main_applock_menu_item:
                                boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
                                if(isFirstLock){
                                    startActivity(new Intent(MainActivity.this, GestureCreateActivity.class));
                                }else{
                                    startActivity(new Intent(MainActivity.this, AppLockActivity.class));
                                }
                                break;
                            case R.id.main_setting_menu_item:
                                startActivity(new Intent(MainActivity.this, SettingLockActivity.class));
                                break;
                            case R.id.main_harassintercept_menu_item:
                                //首次进入做一次数据库更新
                                boolean isUpdate = (boolean) SpHelper.getInstance().get(Constant.UPDATE_SQLITE,false);
                                if(!isUpdate){
                                    io.reactivex.Observable.create(new ObservableOnSubscribe<Boolean>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                            AddressListManager.getmInstance().updateAddressListSqliteData();
                                            CallLogManager.getmInstance().updateCallLogSqliteData();
                                            SMSManager.getmInstance().updateSMSSqliteData();
                                            SpHelper.getInstance().put(Constant.UPDATE_SQLITE,true);
                                            e.onNext(true);
                                        }
                                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean aBoolean) throws Exception {
                                            if(aBoolean){
                                                //停留5秒
                                                SystemClock.sleep(5000);
                                                startActivity(new Intent(MainActivity.this, HarassInterceptActivity.class));
                                            }
                                        }
                                    });
                                }else{
                                    startActivity(new Intent(MainActivity.this, HarassInterceptActivity.class));
                                }
                                break;

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
        final String GOOGLE_PLAY = "com.android.vending";
        Uri uri = Uri.parse("market://details?id=com.jerrywang.phonehelper");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage(GOOGLE_PLAY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 5.x以上系统启用 JobScheduler API 进行实现守护进程的唤醒操作
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startJobScheduler() {
        int jobId = 1;
        JobInfo.Builder jobInfo = new JobInfo.Builder(jobId, new ComponentName(this, VMDaemonJobService.class));
        jobInfo.setPeriodic(10000);
        jobInfo.setPersisted(true);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo.build());
    }

}
