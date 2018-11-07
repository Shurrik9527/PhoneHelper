package com.jerrywang.phonehelper.applock.gesturelock.unlock;

import android.content.Context;
import android.os.Bundle;
import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 创建应用锁接口
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public interface GestureUnlockContract {

    interface View extends BaseView<GestureUnlockContract.Presenter> {

        void initLockPatternView();

        void onBackKey();

    }

    interface Presenter extends BasePresenter {

    }
}