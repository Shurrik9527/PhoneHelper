package com.jerrywang.phonehelper.applock.gesturelock.setting;

import android.content.Context;
import android.os.Bundle;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;
import com.jerrywang.phonehelper.bean.enums.LockStage;
import com.jerrywang.phonehelper.widget.LockPatternView;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁设置接口
 * @date 2018/10/14
 * @email 252774645@qq.com
 */
public interface SettingLockContract {

    interface View {
        void initData();
        void openAppLock(boolean state);
        void lockScreenAfterAddlock(boolean state);
        void resetPassword();
        void showTop(String msg);
    }

}