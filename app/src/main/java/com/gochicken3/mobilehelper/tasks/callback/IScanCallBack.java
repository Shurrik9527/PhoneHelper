package com.gochicken3.mobilehelper.tasks.callback;

import com.gochicken3.mobilehelper.bean.JunkCleanerInformBean;

import java.util.ArrayList;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 扫描回调
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public interface IScanCallBack {
    void onBegin();

    void onProgress(JunkCleanerInformBean junkCleanerInformBean);

    void onCancel();

    void onFinish(ArrayList<JunkCleanerInformBean> apkList, ArrayList<JunkCleanerInformBean> logList, ArrayList<JunkCleanerInformBean> tempList);

    void onOverTime();

}
