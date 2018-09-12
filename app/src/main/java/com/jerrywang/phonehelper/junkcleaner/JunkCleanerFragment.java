package com.jerrywang.phonehelper.junkcleaner;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.JunkCleanerGroupBean;
import com.jerrywang.phonehelper.bean.JunkCleanerMultiItemBean;
import com.jerrywang.phonehelper.util.ToastUtil;
import com.jerrywang.phonehelper.widget.RadarScanView;
import com.jerrywang.phonehelper.widget.dialog.JunkCleanerDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 Fragment
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerFragment extends Fragment implements JunkCleanerContract.View {

    private  static  final String TAG =JunkCleanerFragment.class.getName();
    @BindView(R.id.junkclearner_rsv)
    RadarScanView junkclearnerRsv;
    @BindView(R.id.junkclearner_elv)
    ExpandableListView junkclearnerElv;
    @BindView(R.id.junkclearner_start_iv)
    ImageView junkclearnerStartIv;
    @BindView(R.id.junkclearner_current_scan_tv)
    TextView  junkcleanerCurrentScanTv;
    Unbinder unbinder;
    private JunkCleanerExpandAdapter mAdapter;
    private JunkCleanerContract.Presenter presenter;
    private JunkCleanerDialog mJunkCleanerDialog;
    private boolean isStart =true;
    public JunkCleanerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JunkCleanerFragment newInstance() {
        JunkCleanerFragment fragment = new JunkCleanerFragment();
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
        View root = inflater.inflate(R.layout.junkclearner_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        initView();
        new JunkCleanerPresenter(this);
        initData();
        initClickListener();
        return root;
    }

    @Override
    public void setPresenter(JunkCleanerContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void initView() {

        junkclearnerRsv.setCircleColor(getResources().getColor(R.color.radarscan_white));
        junkclearnerRsv.setLineColor(getResources().getColor(R.color.radarscan_white));



    }

    @Override
    public void initClickListener() {

        junkclearnerStartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart =false;
                if(presenter!=null){
                    presenter.startCleanJunkTask(mAdapter.getData());
                }
            }
        });

    }

    @Override
    public void initData() {

        if(presenter!=null){
            //初始化适配器数据
            presenter.initAdapterData();
            //开始雷达扫描任务
            if(junkclearnerRsv!=null){
                junkclearnerRsv.start();
            }
            presenter.startScanTask();
        }

        //心跳
        startHeartBeat();
    }

    @Override
    public void showTotalSize(String size) {
        if(TextUtils.isEmpty(size))
            return;
        if(junkclearnerRsv!=null){
            junkclearnerRsv.stop();
            if(size.contains("M")){
                String[] sizes =size.split("M");
                if(TextUtils.isEmpty(sizes[0])){
                    junkclearnerRsv.showScore(0.0f);
                }else{
                    junkclearnerRsv.showScore(Float.valueOf(sizes[0]));
                }
            }
        }
    }

    @Override
    public void showCurrentScanJunkFileName(String path) {
        if(junkcleanerCurrentScanTv!=null){
            junkcleanerCurrentScanTv.setText(path);
        }
    }

    @Override
    public void showItemTotalJunkSize(int index, String junkSize) {
        if(mAdapter!=null){
            mAdapter.setItemTotalSize(index,junkSize);
        }
    }

    @Override
    public void showData(JunkCleanerGroupBean junkCleanerGroupBean) {
        if(junkCleanerGroupBean!=null){
            if(mAdapter!=null){
                mAdapter.setData(junkCleanerGroupBean);

                //隐藏按钮
                junkclearnerStartIv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showDialog(int index) {
        if(mAdapter!=null){
            mAdapter.showItemDialog();
        }
    }

    @Override
    public void dissDialog(int index) {
        if(mAdapter!=null){
            mAdapter.dismissItemDialog(index);
        }
    }

    @Override
    public void clickGroup(boolean isExpand, int index) {
        if (!isExpand) {
            junkclearnerElv.expandGroup(index);
        } else {
            junkclearnerElv.collapseGroup(index);
        }
    }

    @Override
    public void initAdapterData(List<JunkCleanerMultiItemBean> mlists) {

        mAdapter = new JunkCleanerExpandAdapter(getActivity(),mlists);
        junkclearnerElv.setGroupIndicator(null);
        junkclearnerElv.setChildIndicator(null);
        junkclearnerElv.setDividerHeight(0);
        junkclearnerElv.setAdapter(mAdapter);
    }

    @Override
    public void cleanFinish() {
        mJunkCleanerDialog = new JunkCleanerDialog(getContext());
        mJunkCleanerDialog.show();
        mJunkCleanerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mJunkCleanerDialog.dismiss();
                getActivity().finish();
            }
        });

    }

    @Override
    public void startHeartBeat() {

        new Thread(){
            public void run() {
                while (isStart){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                playHeartbeatAnimation();
                            }
                        });
                    }
                }
            };
        }.start();
    }

    @Override
    public void showMessageTips(String msg) {
        if(TextUtils.isEmpty(msg))
            return;
        ToastUtil.showToast(getActivity(),msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(presenter!=null){
            presenter.unsubscribe();
        }
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
                if(junkclearnerStartIv!=null){
                    junkclearnerStartIv.startAnimation(animationSet);
                }
            }
        });
        // 实现心跳的View
        if(junkclearnerStartIv!=null){
            junkclearnerStartIv.startAnimation(animationSet);
        }
    }

    @Override
    public void stopScan() {

    }

}
