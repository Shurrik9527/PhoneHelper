package com.jerrywang.phonehelper.broadcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.jerrywang.phonehelper.bean.SmsBean;
import com.jerrywang.phonehelper.event.RefreshRewordEvent;
import com.jerrywang.phonehelper.event.RefreshSMSEvent;
import com.jerrywang.phonehelper.manager.HarassInterceptManager;
import com.jerrywang.phonehelper.manager.SMSManager;
import com.jerrywang.phonehelper.util.RxBus.RxBus;

import java.util.ArrayList;
import java.util.List;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 短信拦截广播
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class SmsReciever extends BroadcastReceiver {
    private static  final  String TAG = SmsReciever.class.getName();

    SmsReceiverProcessor mSmsReceiverProcessor;

    public SmsReciever() {
        mSmsReceiverProcessor = new SmsReceiverProcessor();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null){
            return;
        }
        Bundle bundle = intent.getExtras();
        if(bundle==null){
            return;
        }

        String action = intent.getAction();
        if (SmsReceiverProcessor.ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED.equals(action)
                || SmsReceiverProcessor.ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED2.equals(action)
                || SmsReceiverProcessor.ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED_2.equals(action)
                || SmsReceiverProcessor.ANDROID_PROVIDER_TELEPHONY_GSM_SMS_RECEIVED.equals(action)){

            SmsBean bean =mSmsReceiverProcessor.handleSms(intent);
            if(bean!=null){
                HarassInterceptManager manager = new HarassInterceptManager(context);
                AudioManager mAudioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                if(!TextUtils.isEmpty(bean.getAddress())){
                    if(manager.isSMSBlack(bean.getAddress())){//拉黑的
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        //不往后面传递了
                        abortBroadcast();
                        Log.i(TAG,"已拉黑");
                        bean.setBack(true);
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }else{
//                        List<SmsBean> list = new ArrayList<>();
//                        list.add(bean);
//                        if(manager!=null){
//                            try {
//                                manager.instanceAllSMSInfoTable(list);
//                            } catch (PackageManager.NameNotFoundException e) {
//                                e.printStackTrace();
//                                Log.i(TAG,"NameNotFoundException=="+e.getMessage());
//                            }
//                        }
                        SMSManager.getmInstance().updateSMSSqliteData();
                    }
                }
            }
        }
    }
}
