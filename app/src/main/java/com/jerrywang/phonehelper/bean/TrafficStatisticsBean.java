package com.jerrywang.phonehelper.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 流量统计基类
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisticsBean implements Serializable{

    private String mName;                   //名称
    private String mPackageName;            //包名
    private Drawable mDrawable;            //图标
    private String totalSize;                 //总大小
    private String wifiSize;                 //wifi大小
    private String mobileSize;                 //mobile总大小

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getWifiSize() {
        return wifiSize;
    }

    public void setWifiSize(String wifiSize) {
        this.wifiSize = wifiSize;
    }

    public String getMobileSize() {
        return mobileSize;
    }

    public void setMobileSize(String mobileSize) {
        this.mobileSize = mobileSize;
    }

    @Override
    public String toString() {
        return "TrafficStatisticsBean{" +
                "mName='" + mName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mDrawable=" + mDrawable +
                ", totalSize='" + totalSize + '\'' +
                ", wifiSize='" + wifiSize + '\'' +
                ", mobileSize='" + mobileSize + '\'' +
                '}';
    }
}
