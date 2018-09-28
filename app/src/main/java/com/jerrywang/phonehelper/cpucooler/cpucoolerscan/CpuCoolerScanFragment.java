package com.jerrywang.phonehelper.cpucooler.cpucoolerscan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;
import com.jerrywang.phonehelper.cpucooler.CpuCoolerActivity;
import com.jerrywang.phonehelper.cpucooler.CpuCoolerAdapter;
import com.jerrywang.phonehelper.cpucooler.CpuCoolerContract;
import com.jerrywang.phonehelper.cpucooler.CpuCoolerPresenter;
import com.jerrywang.phonehelper.cpucooler.cpucoolersuccess.CpuCoolerSuccessActivity;
import com.jerrywang.phonehelper.junkcleaner.optimized.OptimizedActivity;
import com.jerrywang.phonehelper.main.MainActivity;
import com.jerrywang.phonehelper.util.SharedPreferencesHelper;
import com.jerrywang.phonehelper.util.TimeUtil;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CpuCoolerScanFragment extends Fragment implements CpuCoolerScanContract.View {
    private static final String TAG = CpuCoolerScanFragment.class.getName();
    @BindView(R.id.cpucooler_thermometer_iv)
    ImageView cpucoolerThermometerIv;
    @BindView(R.id.cpu_scan_iv)
    ImageView cpuScanIv;
    @BindView(R.id.cpucooler_background_rl)
    RelativeLayout cpucoolerBackgroundRl;

    private CpuCoolerScanContract.Presenter presenter;

    private float temp;
    private SharedPreferencesHelper mSP;

    public CpuCoolerScanFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CpuCoolerScanFragment newInstance() {
        CpuCoolerScanFragment fragment = new CpuCoolerScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        new CpuCoolerScanPresenter(this);

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
        if(cpuScanIv!=null){
            cpuScanIv.clearAnimation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.cpucooler_scan_dialo_layout, container, false);
        ButterKnife.bind(this, root);
        if (isOptimized()) {
            Intent mIntent = new Intent(getActivity(), OptimizedActivity.class);
            mIntent.putExtra("BUNDLE", getResources().getString(R.string.cpucooler_title));
            startActivity(mIntent);
            getActivity().finish();
        } else {
            initView();
            initData();
        }
        return root;
    }

    @Override
    public void setPresenter(CpuCoolerScanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        int width =cpucoolerThermometerIv.getLayoutParams().width;
        int height =cpucoolerThermometerIv.getLayoutParams().height;
        RelativeLayout.LayoutParams param =new RelativeLayout.LayoutParams(5 * width / 8, 5 * height / 8);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        cpucoolerThermometerIv.setLayoutParams(param);
        if(cpuScanIv!=null){
            ObjectAnimator animator = ObjectAnimator.ofFloat(cpuScanIv, "translationY", 0, 150, -200,0);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public void showProcessRunning(List<AppProcessInfornBean> mlists) {

        if(cpuScanIv!=null){
            cpuScanIv.clearAnimation();
        }
        Bundle mBundle = new Bundle();
        mBundle.putFloat("TEMP",temp);
        MainActivity.cpuLists =mlists;
        if(getActivity()!=null){
            Intent mIntent = new Intent(getActivity(), CpuCoolerActivity.class);
            mIntent.putExtra("BUNDLE",mBundle);
            startActivity(mIntent);
            getActivity().finish();
        }

    }

    @Override
    public void showTemperature(float temper) {
        this.temp = temper;
    }


    @Override
    public void initData() {
        if (presenter != null) {
            presenter.startScanProcessRunningApp();
            presenter.getProcessRunningApp();
        }
    }


    @Override
    public boolean isOptimized() {
        //获取上次清理保存时间
        mSP = new SharedPreferencesHelper(getActivity());
        if (mSP != null) {
            String lastTime = (String) mSP.getSharedPreference(Constant.SAVE_CPU_COOLER_TIME, "");
            if (!TextUtils.isEmpty(lastTime) && !TimeUtil.isTrue(lastTime, TimeUtil.currentTimeStr(), 1000 * 60 * 5)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
