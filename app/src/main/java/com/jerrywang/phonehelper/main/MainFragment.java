package com.jerrywang.phonehelper.main;

import android.Manifest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.junkcleaner.JunkCleanerActivity;
import com.jerrywang.phonehelper.appmanager.AppManagerActivity;
import com.jerrywang.phonehelper.chargebooster.ChargeBoosterActivity;
import com.jerrywang.phonehelper.cpucooler.CpuCoolerActivity;
import com.jerrywang.phonehelper.util.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


public class MainFragment extends Fragment implements MainContract.View {

    private MainContract.Presenter presenter;

    @BindView(R.id.lav_phonebooster)
    LottieAnimationView lavPhoneBooster;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.cv_junkcleaner)
    @Override
    public void showJunkCleaner() {
        //垃圾清理
        if (presenter != null) {
            presenter.startActivity(getActivity(), JunkCleanerActivity.class, null);
        }

    }

    @OnClick(R.id.cv_appmanager)
    @Override
    public void showAppManager() {
        //应用管理
        Intent intent = new Intent(getContext(), AppManagerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cv_cpucooler)
    @Override
    public void showCpuCooler() {
        //手机降温
        Intent intent = new Intent(getContext(), CpuCoolerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.lav_phonebooster)
    @Override
    public void showPhoneBooster() {
        //优化加速
        ToastUtil.showToast(getContext(), "优化加速");
        //播放动画
        lavPhoneBooster.playAnimation();
        //停止动画
        //lavPhoneBooster.cancelAnimation();
    }


    @OnClick(R.id.cv_chargebooster)
    @Override
    public void showChargeBooster() {
        //智能充电
        Intent intent = new Intent(getActivity(), ChargeBoosterActivity.class);
        startActivity(intent);
    }



    @Override
    public void initView() {
        //手动授予权限  目前未授予权限则退出APP
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CLEAR_APP_CACHE, Manifest.permission.DELETE_CACHE_FILES)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Log.i("MainFragment", "MainFragment 授权成功");
                        }
                    }
                });

    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
