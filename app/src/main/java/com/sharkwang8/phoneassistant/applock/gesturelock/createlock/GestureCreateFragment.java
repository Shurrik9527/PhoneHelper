package com.sharkwang8.phoneassistant.applock.gesturelock.createlock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.sharkwang8.phoneassistant.R;
import com.sharkwang8.phoneassistant.applock.AppLockActivity;
import com.sharkwang8.phoneassistant.base.Constant;
import com.sharkwang8.phoneassistant.bean.enums.LockStage;
import com.sharkwang8.phoneassistant.service.LockService;
import com.sharkwang8.phoneassistant.util.EventUtil;
import com.sharkwang8.phoneassistant.util.LockPatternUtils;
import com.sharkwang8.phoneassistant.util.LockPatternViewPattern;
import com.sharkwang8.phoneassistant.util.SpHelper;
import com.sharkwang8.phoneassistant.util.ToastUtil;
import com.sharkwang8.phoneassistant.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 创建应用锁 界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class GestureCreateFragment extends Fragment implements GestureCreateContract.View {


    @BindView(R.id.gesturecreate_lock_icon)
    ImageView gesturecreateLockIcon;
    @BindView(R.id.gesturecreate_tip_tv)
    TextView gesturecreateTipTv;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.gesturecreate_lpv)
    LockPatternView mLockPatternView;
    @BindView(R.id.gesturecreate_reset_btn)
    Button gesturecreateResetBtn;

    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreateContract.Presenter mPresenter;

    public GestureCreateFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GestureCreatePresenter(this,getActivity());
    }

    // TODO: Rename and change types and number of parameters
    public static GestureCreateFragment newInstance() {
        GestureCreateFragment fragment = new GestureCreateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gesturecreate_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    @Override
    public void updateLockTip(String text, boolean isToast) {
        if(gesturecreateTipTv!=null){
            gesturecreateTipTv.setText(text);
        }
    }

    @Override
    public void setHeaderMessage(int headerMessage) {
        if(gesturecreateTipTv!=null){
            gesturecreateTipTv.setText(headerMessage);
        }
    }

    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if(mLockPatternView!=null){
            if (patternEnabled) {
                mLockPatternView.enableInput();
            } else {
                mLockPatternView.disableInput();
            }
            mLockPatternView.setDisplayMode(displayMode);
        }
    }

    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    @Override
    public void ChoiceTooShort() {
        showMessageTips(getString(R.string.gesturecreate_password_small));//路径太短
    }

    @Override
    public void moveToStatusTwo() {

    }

    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    @Override
    public void ConfirmWrong() {
        showMessageTips(getString(R.string.gesturecreate_password_fail));//路径太短
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 100);
    }

    @Override
    public void ChoiceConfirmed() {

        showMessageTips(getString(R.string.gesturecreate_password_success));
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        EventUtil.sendEvent(getActivity(), AFInAppEventType.START_TRIAL, "Gesture password has been created!");
        clearPattern();
        if(getActivity()!=null){
            startActivity(getActivity(), AppLockActivity.class,null);
        }
    }

    @Override
    public void startActivity(Context mContext, Class<?> mclass, Bundle mBundle) {

        SpHelper.getInstance().put(Constant.LOCK_STATE,true);////开启应用锁开关
        getActivity().startService(new Intent(getActivity(), LockService.class));
        SpHelper.getInstance().put(Constant.LOCK_IS_FIRST_LOCK, false);//第一次设置成功
        startActivity(new Intent(mContext,mclass));
        getActivity().finish();
    }

    @Override
    public void setStepOne() {

        if(gesturecreateTipTv!=null){
            gesturecreateTipTv.setText(getString(R.string.lock_recording_intro_header));
        }
        if(mPresenter!=null){
            mPresenter.updateStage(LockStage.Introduction);
        }
    }

    @Override
    public void setPresenter(GestureCreateContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void initView() {
        mLockPatternUtils = new LockPatternUtils(getActivity());
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if(mPresenter!=null){
                    mPresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getActivity(),msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.gesturecreate_reset_btn)
    public void onClick() {
        setStepOne();
    }


    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.onDestroy();
        }
    }
}
