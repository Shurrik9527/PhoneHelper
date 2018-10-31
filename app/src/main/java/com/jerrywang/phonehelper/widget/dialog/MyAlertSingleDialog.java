package com.jerrywang.phonehelper.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;


/**
 * [类功能说明]
 * 确认弹框
 * @author HeGuoGui
 * @version 2.1.0
 * @time 2017/7/25 0025
 */
public class MyAlertSingleDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String content;
    private ConfirmListener confirmListener;
    private CancelListener cancelListener;
    protected TextView titleTv;
    private TextView contentTv;
    private TextView confirmTv;
    private String btnStr;

    public MyAlertSingleDialog(Context context, String title, String content, String btnstr,ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.title = title;
        this.content = content;
        this.confirmListener = confirmListener;
        this.btnStr=btnstr;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_single_no_image_alert_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        confirmTv = (TextView) findViewById(R.id.confirm_tv);
        confirmTv.setOnClickListener(this);

        if(TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }else {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(btnStr)){
            confirmTv.setText(btnStr);
        }
        contentTv.setText(content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tv: {
                confirmListener.callback();
            }
            break;
        }
        dismiss();
    }

    public interface ConfirmListener {
        void callback();
    }

    public interface CancelListener {
        void callback();
    }

}
