package com.sharkwang8.phoneassistant.junkcleaner.junkcleanersuccess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharkwang8.phoneassistant.R;
import com.sharkwang8.phoneassistant.util.AdUtil;
import com.sharkwang8.phoneassistant.util.ToastUtil;
import com.sharkwang8.phoneassistant.widget.DigitalRollingTextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 降温成功Fragment
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class JunkCleanerSuccessFragment extends Fragment implements JunkCleanerSuccessContract.View {


    private static final String TAG =JunkCleanerSuccessFragment.class.getName();
    @BindView(R.id.junkclearn_success_tv)
    DigitalRollingTextView cpucoolerSuccessTv;
    @BindView(R.id.junkclearn_success_iv)
    ImageView cpucoolerSuccessIv;
    @BindView(R.id.junkclearn_success_rotate_iv)
    ImageView cpucoolerSuccessRotateIv;
    @BindView(R.id.junkclearn_time_tv)
    TextView cpucoolerTimeTv;

    private JunkCleanerSuccessContract.Presenter presenter;
    private  String mTemp =null;


    public JunkCleanerSuccessFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JunkCleanerSuccessFragment newInstance() {
        JunkCleanerSuccessFragment fragment = new JunkCleanerSuccessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle mBundle =getArguments();
            if(mBundle!=null){
                mTemp =mBundle.getString("BUNDLE");
            }
        }
        new JunkCleanerSuccessPresenter(this);
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

        View root = inflater.inflate(R.layout.junkclearn_success_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        startScan();
        return root;
    }

    @Override
    public void setPresenter(JunkCleanerSuccessContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {


        if(cpucoolerSuccessTv!=null){
            cpucoolerSuccessTv.setModleType(DigitalRollingTextView.ModleType.MONEY_TYPE);
            cpucoolerSuccessTv.setText("0.00");
        }
        if(cpucoolerTimeTv!=null){
            cpucoolerTimeTv.setText("Junk Files Cleaned");
        }
        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_chean_anim);
        cpucoolerSuccessRotateIv.startAnimation(rotate);

        //广告
        AdUtil.showAds(getActivity(), "JunkCleanerSuccessFragment.initView()");
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }



    @Override
    public void startScan() {
        if(presenter!=null){
            presenter.startScanTask();
        }
    }

    @Override
    public void startRotateAnimation() {

    }

    @Override
    public void showTotalSize(String size) {

        if(TextUtils.isEmpty(size)){
            return;
        }

        float msize =0.0f;
        if(size.contains("M")){
            String[] sizes =size.split("M");
            if(!TextUtils.isEmpty(sizes[0])){
                msize =Float.valueOf(sizes[0]);
            }
        }

        if(cpucoolerSuccessRotateIv!=null){
            cpucoolerSuccessRotateIv.clearAnimation();
            cpucoolerSuccessRotateIv.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(mTemp)) {
            float temp = Float.parseFloat(mTemp);
            BigDecimal bigDecimal = new BigDecimal(Math.abs(msize - temp));
            float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            cpucoolerSuccessTv.setContent(dropped + "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



}
