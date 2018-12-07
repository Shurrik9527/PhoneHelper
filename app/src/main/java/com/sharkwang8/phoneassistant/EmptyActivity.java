package com.sharkwang8.phoneassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.sharkwang8.phoneassistant.event.EmptyEvent;
import com.sharkwang8.phoneassistant.util.AdUtil;
import com.sharkwang8.phoneassistant.util.RxBus.RxBusHelper;

import io.reactivex.disposables.CompositeDisposable;

public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        RxBusHelper.doOnMainThread(EmptyEvent.class, new CompositeDisposable(), new RxBusHelper.OnEventListener<EmptyEvent>() {
            @Override
            public void onEvent(EmptyEvent junkCleanerTotalSizeEvent) {
                finish();
            }
        });
        AdUtil.getAdTypeAndShow(EmptyActivity.this, "EmptyActivity");
        setContentView(new View(this));
    }
}
