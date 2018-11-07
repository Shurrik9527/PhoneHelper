package com.jerrywang.phonehelper.trafficstatistics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.jerrywang.phonehelper.util.FormatUtil;

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
        if(!TextUtils.isEmpty(item.getmName())){
            helper.setText(R.id.trafficstatistic_name_tv,item.getmName());
        }else{
            helper.setText(R.id.trafficstatistic_name_tv,"未知App");
        }


        if(item.getWifiSize()==0){
            helper.setText(R.id.trafficstatistic_wifisize_tv,"wifi:0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getWifiSize());
            helper.setText(R.id.trafficstatistic_wifisize_tv,"wifi:"+ mFileSize.mSize+mFileSize.mUnit.name());
        }


        if(item.getMobileSize()==0){
            helper.setText(R.id.trafficstatistic_mobilesize_tv,"mobile:0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getMobileSize());
            helper.setText(R.id.trafficstatistic_mobilesize_tv,"mobile:"+ mFileSize.mSize+mFileSize.mUnit.name());
        }

        if(item.getTotalSize()==0){
            helper.setText(R.id.trafficstatistic_total_tv,"0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getTotalSize());
            helper.setText(R.id.trafficstatistic_total_tv,mFileSize.mSize+mFileSize.mUnit.name()+"");
        }


    }
}
