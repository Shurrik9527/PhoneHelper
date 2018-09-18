package com.jerrywang.phonehelper.util;

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

}
