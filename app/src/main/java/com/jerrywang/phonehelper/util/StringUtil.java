package com.jerrywang.phonehelper.util;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.jerrywang.phonehelper.bean.CommLockInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/18
 * @email 252774645@qq.com
 */
public class StringUtil {

    private static long lastClickTime;
    /**
     * 防止多次点击事件处理
     * @return
     * @author songdiyuan
     */
    public synchronized static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 清除重复的数据
     * @param lockInfos
     * @return
     */
    public static List<CommLockInfo> clearRepeatCommLockInfo(List<CommLockInfo> lockInfos) {
        HashMap<String, CommLockInfo> hashMap = new HashMap<>();
        for (CommLockInfo lockInfo : lockInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<CommLockInfo> commLockInfos = new ArrayList<>();
        for (HashMap.Entry<String, CommLockInfo> entry : hashMap.entrySet()) {
            commLockInfos.add(entry.getValue());
        }
        return commLockInfos;
    }


    /**
     * 搜索View
     * @param searchView
     * @return
     */
    public static Observable<String> fromSearchView(final SearchView searchView){

        final BehaviorSubject<String> subject = BehaviorSubject.createDefault("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                subject.onNext(s);
                return true;
            }
        });
        return  subject;
    }





    /**
     * 是否是中文
     * @param c
     * @return
     */
    public static boolean isChinese(char c){
        return c>=0x4e00&&c<=0x9FA5;
    }

    /**
     * 是否含有某个中文
     * @param str
     * @return
     */
    public static boolean isContainChina(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }
        for(char c:str.toCharArray()){
            if(isChinese(c)){
                return true;
            }
        }
        return false;
    }


    public static  boolean isEquals(String org,String des){
        if(TextUtils.isEmpty(org)||TextUtils.isEmpty(des)){
            return false;
        }

        StringBuffer orgsb =new StringBuffer();
        StringBuffer dessb = new StringBuffer();

        return false;
    }


}
