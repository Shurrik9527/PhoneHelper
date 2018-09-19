package com.jerrywang.phonehelper.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 时间工具
 * @date 2018/9/18
 * @email 252774645@qq.com
 */
public class TimeUtil {

    private static SimpleDateFormat formatter = null;

    /**
     * 获取当前时间
     * @return
     */
    public static String currentTimeStr(){
        formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }


    /**
     * 获取当前时间
     * @return
     */
    public static Date currentTimeData(){
        return new Date(System.currentTimeMillis());
    }


    /**
     * 是否超过规定时间
     * @param beginTime 开始时间
     * @param endTime
     * @param num
     * @return
     */
    public static  boolean isTrue(String beginTime,String endTime,long num){
        if(TextUtils.isEmpty(beginTime)||TextUtils.isEmpty(endTime)){
            return false;
        }
        formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date mBeginTime=formatter.parse(beginTime);
            Date mEndTime=formatter.parse(endTime);

            if((mEndTime.getTime()-mBeginTime.getTime())>num){//超过num时间
                return true;
            }else{
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }





}
