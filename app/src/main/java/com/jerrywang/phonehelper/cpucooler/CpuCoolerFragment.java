package com.jerrywang.phonehelper.cpucooler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;
import com.jerrywang.phonehelper.cpucooler.cpucoolersuccess.CpuCoolerSuccessActivity;
import com.jerrywang.phonehelper.widget.dialog.CpuCleanDialog;
import com.jerrywang.phonehelper.widget.dialog.CpuScanDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CpuCoolerFragment extends Fragment implements CpuCoolerContract.View {
    private static final String TAG = CpuCoolerFragment.class.getName();
    @BindView(R.id.cpucooler_thermometer_tv)
    TextView cpucoolerThermometerTv;
    @BindView(R.id.cpucooler_thermometer_top_ll)
    LinearLayout cpucoolerThermometerTopLl;
    @BindView(R.id.cpucooler_start_iv)
    ImageView cpucoolerStartIv;
    @BindView(R.id.cpucooler_thermometer_rv)
    RecyclerView cpucoolerThermometerRv;
    @BindView(R.id.cpucooler_cloud_iv)
    ImageView cpucoolerCloudIv;
    @BindView(R.id.cpucooler_speed_iv)
    ImageView cpucoolerSpeedIv;
    @BindView(R.id.cpucooler_rocket_iv)
    ImageView cpucoolerRocketIv;
    private CpuCoolerAdapter mAdapter;
    private CpuScanDialog mCpuScanDialog;
    private CpuCoolerContract.Presenter presenter;
    private boolean isStart = true;
    private List<AppProcessInfornBean> mLists;
    private CpuCleanDialog mCpuCleanDialog;
    private Handler mHandler = new Handler();
    private float temp;

    public CpuCoolerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CpuCoolerFragment newInstance() {
        CpuCoolerFragment fragment = new CpuCoolerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        new CpuCoolerPresenter(this);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.cpucooler_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void setPresenter(CpuCoolerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        startScanApp();
        startHeartBeat();
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public void showProcessRunning(List<AppProcessInfornBean> mlists) {
        if (mAdapter != null && mlists != null) {
            this.mLists = mlists;
            if (mCpuScanDialog != null) {
                mCpuScanDialog.dismiss();
                mCpuScanDialog = null;
            }
            mAdapter.setNewData(mlists);
        }
    }

    @Override
    public void showTemperature(float temper) {
        this.temp =temper;
        cpucoolerThermometerTv.setText(temper + "°C");
    }

    @Override
    public void startScanApp() {
        if (mCpuScanDialog == null) {
            mCpuScanDialog = new CpuScanDialog();
        }
        mCpuScanDialog.show(getActivity().getFragmentManager(), "cpuscan");
    }

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cpucoolerThermometerRv.setLayoutManager(layoutManager);
        mAdapter = new CpuCoolerAdapter(getActivity());
        cpucoolerThermometerRv.setAdapter(mAdapter);
        if (presenter != null) {
            presenter.startScanProcessRunningApp();
            presenter.getProcessRunningApp();
        }
    }

    @Override
    public void startHeartBeat() {
        new Thread() {
            public void run() {
                while (isStart) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                playHeartbeatAnimation();
                            }
                        });
                    }
                }
            }

            ;
        }.start();
    }

    @Override
    public void playHeartbeatAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        animationSet.setDuration(1000);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.3f, 1.0f, 1.3f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));

                animationSet.setDuration(1000);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                // 实现心跳的View
                if (cpucoolerStartIv != null) {
                    cpucoolerStartIv.startAnimation(animationSet);
                }
            }
        });
        // 实现心跳的View
        if (cpucoolerStartIv != null) {
            cpucoolerStartIv.startAnimation(animationSet);
        }
    }

    @Override
    public void cleanFinish() {
        if (getActivity() != null) {
            //隐藏按钮
            if(cpucoolerStartIv!=null){
                cpucoolerStartIv.setVisibility(View.GONE);
            }
            //开启动画 及延迟 由于时间太快 目的让温度有时间降温
            startRocketAnimation();
        }
    }

    @Override
    public void startCleanDialog() {
        if (mCpuCleanDialog == null) {
            mCpuCleanDialog = new CpuCleanDialog();
        }
        mCpuCleanDialog.show(getActivity().getFragmentManager(), "cpuclean");
    }

    @Override
    public void startRocketAnimation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation rocketAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rocket_anim);
                rocketAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (cpucoolerRocketIv != null) {
                            cpucoolerRocketIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (cpucoolerRocketIv != null) {
                            cpucoolerRocketIv.setVisibility(View.GONE);
                        }

                        Intent mIntent = new Intent(getActivity(), CpuCoolerSuccessActivity.class);
                        if(mIntent!=null){
                            mIntent.putExtra("BUNDLE",temp+"");
                        }
                        startActivity(mIntent);
                        getActivity().finish();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                cpucoolerRocketIv.startAnimation(rocketAnimation);


                Animation cloudAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud_anim);
                cloudAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (cpucoolerCloudIv != null) {
                            cpucoolerCloudIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (cpucoolerCloudIv != null) {
                            cpucoolerCloudIv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                cpucoolerCloudIv.startAnimation(cloudAnimation);


                Animation speedAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.speed_anim);
                speedAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (cpucoolerSpeedIv != null) {
                            cpucoolerSpeedIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (cpucoolerSpeedIv != null) {
                            cpucoolerSpeedIv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                cpucoolerSpeedIv.startAnimation(speedAnimation);
            }
        }, 150);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isStart = false;
    }

    @OnClick(R.id.cpucooler_start_iv)
    public void onClick() {
        isStart = false;
        if (presenter != null) {
            if (mLists != null && mLists.size() > 0) {
                presenter.startCleanApp(mLists);
            }
        }
    }






}
