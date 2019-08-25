package com.example.zhihudaily.ui;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.zhihudaily.R;

public class ViewPagerFrameLayout extends FrameLayout {
    private volatile Controller controller;

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
    private void setIndicatorDot(int count) {
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

    /*
    adapter可以为null
     */
    public Controller getControllerInstance(PagerAdapter adapter) {
        if (controller == null) {
            synchronized (this) {
                if (controller == null) {
                    controller = new Controller(adapter);
                }
            }
        }
        return controller;
    }


    public class Controller {
        private ViewPager viewPager;
        private PagerAdapter adapter;
        private LinearLayout dotLayout;

        private int oldPosition = 0; //上一个位置
        private int currentIndex = 0; //当前位置
        private long time = 3000; //自动播放时间


        private Handler handler = new Handler();
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = adapter.getCount();
                // 无网络连接时，adapter.getCount()返回0，那么就会抛出除数为0的异常
                if (count != 0) {
                    currentIndex = (currentIndex + 1) % count;
                    viewPager.setCurrentItem(currentIndex);
                    play();
                }
            }
        };

        private Controller(PagerAdapter adapter) {
            this.adapter = adapter;
            this.viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(adapter);
            this.dotLayout = findViewById(R.id.dot_layout);
            initViewPager();
        }

        private void initViewPager() {
            viewPager.setCurrentItem(currentIndex);
            // 设置缓存的页面数
            viewPager.setOffscreenPageLimit(1);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentIndex = position;
                    dotLayout.getChildAt(oldPosition).setEnabled(false);
                    dotLayout.getChildAt(currentIndex).setEnabled(true);
                    oldPosition = currentIndex;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {

                    } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {

                    }
                }
            });
            setIndicatorDot(adapter.getCount());
        }

        /**
         * 播放
         */
        public void play() {
            handler.postDelayed(runnable, time);
        }

        /**
         * 取消播放
         */
        public void cancel() {
            handler.removeCallbacks(runnable);
        }

        public void notifyDataSetChanged() {
            dotLayout.removeAllViews();
            setIndicatorDot(adapter.getCount());
            oldPosition = currentIndex = 0;
            adapter.notifyDataSetChanged();
        }
    }
}
