package com.jerrywang.phonehelper.akame.frgment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.akame.MyFragmentViewPageAdapter;
import com.jerrywang.phonehelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment03 extends Fragment {
    private TabLayout tbMenu;
    private ViewPager vpContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_fragment03, container, false);
        tbMenu = view.findViewById(R.id.tb_menu);
        vpContent = view.findViewById(R.id.vp_content);
        MyFragmentViewPageAdapter pageAdapter = new MyFragmentViewPageAdapter(getChildFragmentManager(), getFragmentList(), getTitleList());
        vpContent.setAdapter(pageAdapter);
        tbMenu.setupWithViewPager(vpContent);
        TabLayoutUtil.setIndicator(tbMenu, 10, 10);
        return view;
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < getTitleList().size(); i++) {
            fragments.add(new TestFragment04());
        }
        return fragments;
    }

    private List<String> getTitleList() {
        List<String> lists = new ArrayList<>();
        lists.add("全部");
        lists.add("推荐");
        lists.add("日用");
        lists.add("电器");
        lists.add("数码");
        lists.add("个护");
        lists.add("首饰");
        return lists;
    }

}
