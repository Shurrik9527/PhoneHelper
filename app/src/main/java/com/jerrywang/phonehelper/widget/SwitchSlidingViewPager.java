package com.jerrywang.phonehelper.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Shurrik on 2017/11/24.
 */

public class SwitchSlidingViewPager extends ViewPager {

    private boolean isCanScroll = true;
    private boolean isSmoothScroll = true;

    public SwitchSlidingViewPager(Context context) {
        super(context);
    }

    public SwitchSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        isSmoothScroll = smoothScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            super.setCurrentItem(item, isSmoothScroll);
        }
    }
}
