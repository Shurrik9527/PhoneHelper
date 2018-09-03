package com.jerrywang.phonehelper.akame.frgment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.akame.MyFragmentViewPageAdapter;
import com.jerrywang.phonehelper.widget.SwitchSlidingViewPager;
import com.jerrywang.phonehelper.R;

import java.util.ArrayList;
import java.util.List;


public class TestFragment01 extends Fragment {
    private TabLayout tbMenu;
    private SwitchSlidingViewPager vpContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_fragment01, container, false);
        tbMenu = view.findViewById(R.id.tb_menu);
        vpContent = view.findViewById(R.id.vp_content);
        MyFragmentViewPageAdapter pageAdapter = new MyFragmentViewPageAdapter(getChildFragmentManager(), getFragmentList());
        vpContent.setAdapter(pageAdapter);
        TabLayoutUtil.setIndicator(tbMenu, 40, 40);
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
        return view;
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TestFragment03());
        fragments.add(new TestFragment04());
        return fragments;
    }
}
