package com.gochicken3.mobilehelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.gochicken3.mobilehelper.event.EmptyEvent;
import com.gochicken3.mobilehelper.util.AdUtil;
import com.gochicken3.mobilehelper.util.RxBus.RxBusHelper;

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
