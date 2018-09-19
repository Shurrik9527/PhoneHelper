package com.jerrywang.phonehelper.junkcleaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.bean.JunkCleanerGroupBean;
import com.jerrywang.phonehelper.bean.JunkCleanerMultiItemBean;
import com.jerrywang.phonehelper.junkcleaner.junkcleanersuccess.JunkCleanerSuccessActivity;
import com.jerrywang.phonehelper.junkcleaner.optimized.OptimizedActivity;
import com.jerrywang.phonehelper.util.SharedPreferencesHelper;
import com.jerrywang.phonehelper.util.StringUtil;
import com.jerrywang.phonehelper.util.TimeUtil;
import com.jerrywang.phonehelper.util.ToastUtil;
import com.jerrywang.phonehelper.widget.RadarScanView;
import com.jerrywang.phonehelper.widget.dialog.JunkCleanerDialog;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private String mNum;
    private SharedPreferencesHelper mSP;
    private String  lastTime;
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
                if(StringUtil.isFastDoubleClick()){
                    return;
                }
                isStart =false;
                if(presenter!=null){
                    presenter.startCleanJunkTask(mAdapter.getData());
                }
            }
        });

    }

    @Override
    public void initData() {

        //获取上次清理保存时间
        mSP = new SharedPreferencesHelper(getActivity());
        if(mSP!=null){
            String lastTime= (String) mSP.getSharedPreference(Constant.SAVE_JUNK_CLEANER_TIME,"");
            boolean isAll = (boolean) mSP.getSharedPreference(Constant.SAVE_JUNK_CLEANER_ISALL,true);
            if(!TextUtils.isEmpty(lastTime)&&!TimeUtil.isTrue(lastTime,TimeUtil.currentTimeStr(),1000*60*5)&&isAll){
                //跳转 最佳页面
                Intent mIntent = new Intent(getActivity(), OptimizedActivity.class);
                mIntent.putExtra("BUNDLE",getResources().getString(R.string.junkcleaner_title));
                startActivity(mIntent);
                getActivity().finish();
            }else {
                startScan();
            }
        }else{
            startScan();
        }

    }

    @Override
    public void showTotalSize(String size) {
        if(TextUtils.isEmpty(size))
            return;
        Log.i(TAG,"size=:"+size);
        if(junkclearnerRsv!=null){
            junkclearnerRsv.stop();
            if(size.contains("M")){
                String[] sizes =size.split("M");
                if(TextUtils.isEmpty(sizes[0])){
                    junkclearnerRsv.showScore(0.0f);
                }else{
                    mNum =sizes[0];
                    junkclearnerRsv.showScore(Float.valueOf(sizes[0]));
                }
            }else if(size.contains("B")){
                String[] sizes =size.split("B");
                if(TextUtils.isEmpty(sizes[0])){
                    junkclearnerRsv.showScore(0.0f);
                }else{
                    mNum =sizes[0];
                    junkclearnerRsv.showScore(Float.valueOf(sizes[0]));
                }
            }else{
                junkclearnerRsv.showScore(0.0f);
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
        //保存此时清理状态
        if(mSP!=null){
            mSP.put(Constant.SAVE_JUNK_CLEANER_TIME,TimeUtil.currentTimeStr());
        }

        mJunkCleanerDialog = new JunkCleanerDialog(getContext(), new JunkCleanerDialog.DismissListener() {
            @Override
            public void callBack() {
                if(mJunkCleanerDialog!=null&&mJunkCleanerDialog.isShowing()){
                    mJunkCleanerDialog.dismiss();
                    mJunkCleanerDialog = null;
                }
                Intent mIntent = new Intent(getActivity(), JunkCleanerSuccessActivity.class);
                if(!TextUtils.isEmpty(mNum)){
                    if(mIntent!=null){
                        mIntent.putExtra("BUNDLE",mNum+"");
                    }
                }
                startActivity(mIntent);
                getActivity().finish();
            }
        });
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

    @Override
    public void cleanSpData() {
        if(mSP!=null){
            mSP.remove(Constant.SAVE_JUNK_CLEANER_TIME);
            mSP.remove(Constant.SAVE_JUNK_CLEANER_ISALL);
        }
    }

    @Override
    public void startScan() {
        cleanSpData();
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

}
