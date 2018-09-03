package com.jerrywang.phonehelper.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.main.cart.CartFragment;
import com.jerrywang.phonehelper.main.cart.CartPresenter;
import com.jerrywang.phonehelper.main.classify.ClassifyFragment;
import com.jerrywang.phonehelper.main.classify.ClassifyPresenter;
import com.jerrywang.phonehelper.main.member.MemberFragment;
import com.jerrywang.phonehelper.main.member.MemberPresenter;
import com.jerrywang.phonehelper.main.shop.ShopFragment;
import com.jerrywang.phonehelper.main.shop.ShopPresenter;
import com.jerrywang.phonehelper.util.ToastUtil;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.SwitchSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindColor(R.color.colorPrimary)
    int primary;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindDrawable(R.drawable.ic_menu)
    Drawable menu;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.vp_content)
    SwitchSlidingViewPager vpContent;

    @BindView(R.id.fab_im)
    FloatingActionButton fabIm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        // Set up the statusbar.
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, primary);

        // Set up the toolbar.
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        setupDrawerContent(navigationView);

        // Set up the content
        vpContent.setSmoothScroll(false);
        vpContent.setCanScroll(false);
        vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), getFragmentList()));

        // Set up the bottom menu.

        BottomNavigationBar mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_shop_light, "商城"))
                .addItem(new BottomNavigationItem(R.drawable.ic_classify_light, "分类"))
                .addItem(new BottomNavigationItem(R.drawable.ic_cart_light, "购物车"))
                .addItem(new BottomNavigationItem(R.drawable.ic_user_light, "会员"))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                vpContent.setCurrentItem(position);
                if (position == 2) {
                    fabIm.hide();
                } else {
                    fabIm.show();
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @OnClick(R.id.fab_im)
    public void showImDialog() {
        ToastUtil.showToast(this,"hello");
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        ShopFragment shopFragment = new ShopFragment();
        fragmentList.add(shopFragment);
        new ShopPresenter(shopFragment);
        ClassifyFragment classifyFragment = new ClassifyFragment();
        fragmentList.add(classifyFragment);
        new ClassifyPresenter(classifyFragment);
        CartFragment cartFragment = new CartFragment();
        fragmentList.add(cartFragment);
        new CartPresenter(cartFragment);
        MemberFragment memberFragment = new MemberFragment();
        fragmentList.add(memberFragment);
        new MemberPresenter(memberFragment);
        return fragmentList;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                        }
                        // Close the navigation drawer when an item is selected.
                        //menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList;

        public FragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
