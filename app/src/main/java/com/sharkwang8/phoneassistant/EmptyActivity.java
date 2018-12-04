package com.sharkwang8.phoneassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharkwang8.phoneassistant.event.EmptyEvent;
import com.sharkwang8.phoneassistant.util.AdUtil;
import com.sharkwang8.phoneassistant.util.RxBus.RxBusHelper;

import io.reactivex.disposables.CompositeDisposable;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBusHelper.doOnMainThread(EmptyEvent.class, new CompositeDisposable(), new RxBusHelper.OnEventListener<EmptyEvent>() {
            @Override
            public void onEvent(EmptyEvent junkCleanerTotalSizeEvent) {
                finish();
            }
        });
        AdUtil.getAdTypeAndShow(EmptyActivity.this, "EmptyActivity");
    }
}
