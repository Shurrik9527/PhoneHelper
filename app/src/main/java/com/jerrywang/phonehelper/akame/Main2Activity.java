package com.jerrywang.phonehelper.akame;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.akame.frgment.TestFragment01;
import com.jerrywang.phonehelper.akame.frgment.TestFragment02;
import com.jerrywang.phonehelper.widget.SwitchSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    @BindView(R.id.vp_content)
    SwitchSlidingViewPager vpContent;
    @BindView(R.id.tb_menu)
    TabLayout tbMenu;
    @BindColor(R.color.colorPrimary)
    int primary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        // Set up the statusbar.
        StatusBarUtil.setColor(this, primary);

        MyFragmentViewPageAdapter pageAdapter = new MyFragmentViewPageAdapter(getSupportFragmentManager(), getFragmentList());
        vpContent.setAdapter(pageAdapter);
        tbMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        createTab();
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TestFragment01());
        fragments.add(new TestFragment02());
        return fragments;
    }

    private void createTab() {
        String[] menus = {"源头产品","本地产品","购物车","社交","我的"};
        for (int i = 0; i < 5; i++) {
            TabLayout.Tab tab = tbMenu.newTab();
            if (i == 0) tab.select();
            tbMenu.addTab(tab.setText(menus[i]).setIcon(R.drawable.select_tab_menu));
        }
    }
}
