package com.gochicken3.mobilehelper.harassintercept;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gochicken3.mobilehelper.R;
import com.gochicken3.mobilehelper.bean.SmsBean;
import com.gochicken3.mobilehelper.util.TimeUtil;

import java.util.Date;

/**
 * [类功能说明]
 *
 * @author HeGuoGui
 * @version 3.0.0
 * @time 2017/11/23 0023.
 */

public class SmsAdapter extends BaseQuickAdapter<SmsBean,BaseViewHolder> {

    private Context mContext =null;

    public SmsAdapter(Context context) {
        super(R.layout.harassintercept_sms_item_layout);
        this.mContext =context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SmsBean item) {

        if(item!=null){
            helper.setText(R.id.sms_number_tv,item.getAddress()+"");
            helper.setText(R.id.sms_body_tv,item.getBody()+"");

            if(item.isBack()){
                helper.setTextColor(R.id.sms_number_tv,mContext.getResources().getColor(R.color.red1));
            }else{
                helper.setTextColor(R.id.sms_number_tv,mContext.getResources().getColor(R.color.default_text));
            }

            if(!TextUtils.isEmpty(item.getDate())){
                Date mdate = new Date(Long.parseLong(item.getDate()));
                helper.setText(R.id.sms_time_tv,TimeUtil.timeDefault(mdate)+"");
            }
        }
    }
}
