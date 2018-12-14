package com.gochicken3.mobilehelper.harassintercept;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gochicken3.mobilehelper.R;
import com.gochicken3.mobilehelper.bean.CallLogBean;
import com.gochicken3.mobilehelper.manager.HarassInterceptManager;
import com.gochicken3.mobilehelper.util.TimeUtil;

import java.util.Date;

/**
 * [类功能说明]
 *
 * @author HeGuoGui
 * @version 3.0.0
 * @time 2017/11/23 0023.
 */

public class PhoneAdapter extends BaseQuickAdapter<CallLogBean,BaseViewHolder> {

    private Context mContext =null;
    private HarassInterceptManager manager;
    public PhoneAdapter(Context context) {
        super(R.layout.harassintercept_phone_item_layout);
        this.mContext =context;
        this.manager = new HarassInterceptManager(mContext);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CallLogBean item) {

        if(item!=null){

            //名称为空 则默认填写手机号
            if(TextUtils.isEmpty(item.getNick())){
                helper.setText(R.id.phone_phonenum_tv,item.getPhoneNum()+"");
            }else{
                helper.setText(R.id.phone_phonenum_tv,item.getNick()+"");
            }

            if(!TextUtils.isEmpty(item.getLocation())){
                helper.setText(R.id.phone_fromsource_tv,item.getLocation()+"");
            }

            if(item.isBack()){
                helper.setTextColor(R.id.phone_phonenum_tv,mContext.getResources().getColor(R.color.red1));
            }else{
                helper.setTextColor(R.id.phone_phonenum_tv,mContext.getResources().getColor(R.color.default_text));
            }

            if(!TextUtils.isEmpty(item.getTime())){
                Date mdate = new Date(Long.parseLong(item.getTime()));
                helper.setText(R.id.phone_time_tv, TimeUtil.timeDefault(mdate)+"");
            }


        }
    }
}
