package com.example.zhihudaily.ui;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.zhihudaily.R;

public class ViewPagerFrameLayout extends FrameLayout {

    public ViewPagerFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ViewPagerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /*
        添加viewPager
         */
        ViewPager viewPager = new ViewPager(getContext());
        LayoutParams vparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(vparams);
        viewPager.setId(R.id.view_pager);
        addView(viewPager);

        /*
        添加放置圆点的布局
         */
        LinearLayout dotLayout = new LinearLayout(getContext());
        LayoutParams dparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dparams.gravity = Gravity.BOTTOM;
        dparams.bottomMargin = 10;
        dotLayout.setGravity(Gravity.CENTER);
        dotLayout.setOrientation(LinearLayout.HORIZONTAL);
        dotLayout.setId(R.id.dot_layout);
        addView(dotLayout, dparams);
    }

    /**
     * 设置小圆点
     */
    public void setIndicatorDot(int count) {
        ViewGroup dotLayout = (ViewGroup) findViewById(R.id.dot_layout);
        for (int i = 0; i < count; i++) {
            //添加底部灰点
            View v = new View(getContext());
            v.setBackgroundResource(R.drawable.bg_dot);
            v.setEnabled(false);
            //指定其大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.leftMargin = 20;
            }
            v.setLayoutParams(params);
            dotLayout.addView(v);
        }
        if (dotLayout.getChildCount() != 0) {
            dotLayout.getChildAt(0).setEnabled(true);
        }
    }
}
