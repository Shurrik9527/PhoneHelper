package com.sharkwang8.phoneassistant.harassintercept;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableRow;

import com.sharkwang8.phoneassistant.R;
import com.sharkwang8.phoneassistant.event.RefreshRewordEvent;
import com.sharkwang8.phoneassistant.event.RefreshSMSEvent;
import com.sharkwang8.phoneassistant.manager.CallLogManager;
import com.sharkwang8.phoneassistant.manager.SMSManager;
import com.sharkwang8.phoneassistant.service.HarassInterceptService;
import com.sharkwang8.phoneassistant.util.RxBus.RxBusHelper;

import java.util.ArrayList;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class HarassInterceptFragment extends Fragment implements HarassInterceptContract.View {

    @BindView(R.id.harassintercept_phone_rb)
    RadioButton harassinterceptPhoneRb;
    @BindView(R.id.harassintercept_sms_rb)
    RadioButton harassinterceptSmsRb;
    @BindView(R.id.harassintercept_iv)
    ImageView harassinterceptIv;
    @BindView(R.id.discount_tableRow2)
    TableRow discountTableRow2;
    @BindView(R.id.harassintercept_vp)
    ViewPager harassinterceptVp;
    private HarassInterceptContract.Presenter presenter;


    private HarassInterceptContract.Presenter mPresenter;
    private ArrayList<Fragment> mLists;
    private PhoneFragment mPhoneFragment;
    private SmsFragment mSmsFragment;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private Bundle mBundle;


    public HarassInterceptFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HarassInterceptFragment newInstance() {
        HarassInterceptFragment fragment = new HarassInterceptFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HarassInterceptPresenter(this, getActivity());
        RxBusHelper.doOnMainThread(RefreshRewordEvent.class, new CompositeDisposable(), new RxBusHelper.OnEventListener<RefreshRewordEvent>() {
            @SuppressLint("CheckResult")
            @Override
            public void onEvent(RefreshRewordEvent mRefreshRewordEvent) {
                if (mPhoneFragment != null) {
                    mPhoneFragment.refreshData();
                }
            }
        });

        RxBusHelper.doOnMainThread(RefreshSMSEvent.class, new CompositeDisposable(), new RxBusHelper.OnEventListener<RefreshSMSEvent>() {
            @SuppressLint("CheckResult")
            @Override
            public void onEvent(RefreshSMSEvent mRefreshSMSEvent) {
                if (mSmsFragment != null) {
                    mSmsFragment.refreshData();
                }
            }
        });





    }


    @Override
    public void initData() {
        harassinterceptPhoneRb.setOnClickListener(new MyOnClickListener(0));
        harassinterceptSmsRb.setOnClickListener(new MyOnClickListener(1));
    }

    @Override
    public void initViewPager() {
        Intent mIntent = getActivity().getIntent();
        if (mIntent != null) {
            mBundle = mIntent.getBundleExtra("BUNDLE");
        }
        mLists = new ArrayList<>();
        mPhoneFragment = PhoneFragment.newInstance();
        if (mBundle != null) {
            mPhoneFragment.setArguments(mBundle);
        }

        mSmsFragment = SmsFragment.newInstance();
        if (mBundle != null) {
            mSmsFragment.setArguments(mBundle);
        }

        mLists.add(mPhoneFragment);
        mLists.add(mSmsFragment);
        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager(), mLists);
        harassinterceptVp.setAdapter(mFragmentPagerAdapter);
        harassinterceptVp.setOffscreenPageLimit(1);
        harassinterceptVp.setCurrentItem(0);
        harassinterceptVp.addOnPageChangeListener(new MyViewPagerOnChangeClickListener());
    }

    @Override
    public void initRecycleView() {

    }

    @Override
    public void initImageView() {

        bmpW = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_common_tabimg).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        harassinterceptIv.setImageMatrix(matrix);// 设置动画初始位置
    }

    @Override
    public void showPermissions() {
        NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            getContext().startActivity(intent);
            return;
        }
    }

    @Override
    public void setPresenter(HarassInterceptContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.harassintercept_fragmen, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void initView() {
        initViewPager();
        initRecycleView();
        initImageView();
        showPermissions();
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public class MyViewPagerOnChangeClickListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int one = offset * 2 + bmpW;
            Animation animation = new TranslateAnimation(one * currIndex, one * position, 0, 0);
            currIndex = position;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            harassinterceptIv.startAnimation(animation);
            switch (position) {
                case 0:
                    harassinterceptPhoneRb.setChecked(true);
                    break;
                case 1:
                    harassinterceptSmsRb.setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            harassinterceptVp.setCurrentItem(index);
        }
    }

}
