package com.gochicken3.mobilehelper.harassintercept.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gochicken3.mobilehelper.R;
import com.gochicken3.mobilehelper.bean.SmsBean;
import com.gochicken3.mobilehelper.util.TimeUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/23
 * @email 252774645@qq.com
 */
public class SmsDetailFragment extends Fragment {


    @BindView(R.id.sms_detail_phone_tv)
    TextView smsDetailPhoneTv;
    @BindView(R.id.sms_detail_time_tv)
    TextView smsDetailTimeTv;
    @BindView(R.id.sms_detail_phone_rl)
    RelativeLayout smsDetailPhoneRl;
    @BindView(R.id.sms_detail_body_tv)
    TextView smsDetailBodyTv;
    private SmsBean mTemp;

    public SmsDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SmsDetailFragment newInstance() {
        SmsDetailFragment fragment = new SmsDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle mBundle = getArguments();
            if (mBundle != null) {
                mTemp = (SmsBean) mBundle.getSerializable("BUNDLE");
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sms_detail_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        if(mTemp!=null){

            if(!TextUtils.isEmpty(mTemp.getAddress())){
                smsDetailPhoneTv.setText(mTemp.getAddress());
            }

            if(mTemp.isBack()){
                smsDetailPhoneTv.setTextColor(getResources().getColor(R.color.red1));
            }else{
                smsDetailPhoneTv.setTextColor(getResources().getColor(R.color.default_text));
            }


            if(!TextUtils.isEmpty(mTemp.getDate())){
                Date mdate = new Date(Long.parseLong(mTemp.getDate()));
                smsDetailTimeTv.setText(TimeUtil.timeDefault(mdate)+"");
            }

            if(!TextUtils.isEmpty(mTemp.getBody())){
                smsDetailBodyTv.setText(mTemp.getBody());
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
