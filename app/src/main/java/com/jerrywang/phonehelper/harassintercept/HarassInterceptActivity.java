package com.jerrywang.phonehelper.harassintercept;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import com.jerrywang.phonehelper.BaseActivity;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.base.Constant;
import com.jerrywang.phonehelper.broadcase.SmsReciever;
import com.jerrywang.phonehelper.main.MainActivity;
import com.jerrywang.phonehelper.manager.AddressListManager;
import com.jerrywang.phonehelper.manager.CallLogManager;
import com.jerrywang.phonehelper.manager.SMSManager;
import com.jerrywang.phonehelper.util.SpHelper;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 骚扰拦截
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class HarassInterceptActivity extends BaseActivity{


    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return HarassInterceptFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.harassintercept_top_title));
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
