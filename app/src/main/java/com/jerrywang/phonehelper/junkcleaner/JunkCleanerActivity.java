package com.jerrywang.phonehelper.junkcleaner;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.ToolbarActivity;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JunkCleanerActivity extends ToolbarActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return JunkCleanerFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.contentFragment;
    }


    @Override
    protected void init() {
        super.init();
        setTitle("JunkCleaner");
    }
}
