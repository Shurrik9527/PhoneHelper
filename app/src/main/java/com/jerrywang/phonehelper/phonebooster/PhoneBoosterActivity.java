package com.jerrywang.phonehelper.phonebooster;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.R;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneBoosterActivity extends AppCompatActivity {
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

        PhoneBoosterFragment loginFragment = (PhoneBoosterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_main);

        if (loginFragment == null) {
            loginFragment = PhoneBoosterFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_main, loginFragment);
            transaction.commit();
        }

        new PhoneBoosterPresenter(loginFragment);
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
}
