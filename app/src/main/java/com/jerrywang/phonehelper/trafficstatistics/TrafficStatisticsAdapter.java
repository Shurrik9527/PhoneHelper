package com.jerrywang.phonehelper.trafficstatistics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.CommLockInfo;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;
import com.jerrywang.phonehelper.manager.CommLockInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class TrafficStatisticsAdapter extends BaseQuickAdapter<TrafficStatisticsBean,BaseViewHolder> {


    public TrafficStatisticsAdapter() {
        super(R.layout.trafficstatistics_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrafficStatisticsBean item) {

        ImageView mIcon = helper.getView(R.id.trafficstatistic_icon_iv);
        if(item.getmDrawable()!=null){
            mIcon.setImageDrawable(item.getmDrawable());
        }else{
            mIcon.setImageResource(R.mipmap.ic_launcher);
        }

        helper.setText(R.id.trafficstatistic_name_tv,item.getmName());
        helper.setText(R.id.trafficstatistic_total_tv,item.getTotalSize());
        helper.setText(R.id.trafficstatistic_wifisize_tv,"wifi:"+item.getWifiSize()+"MB");
        helper.setText(R.id.trafficstatistic_mobilesize_tv,"mobile:"+item.getMobileSize()+"MB");

    }
}
