package com.jerrywang.phonehelper.goods.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jerrywang.phonehelper.R;


public class GoodsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail_activity);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        GoodsDetailFragment goodsDetailFragment = (GoodsDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_test);

        if (goodsDetailFragment == null) {
            goodsDetailFragment = GoodsDetailFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_test, goodsDetailFragment);
            transaction.commit();
        }

        new GoodsDetailPresenter(goodsDetailFragment);
    }
}
