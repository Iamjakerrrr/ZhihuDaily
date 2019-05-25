package com.example.zhihudaily.ui;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.example.zhihudaily.R;

public class AutoScrollViewPager {
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ViewPagerFrameLayout viewPagerFrameLayout;
    private LinearLayout dotLayout;

    private int oldPosition = 0; //上一个位置
    private int currentIndex = 0; //当前位置
    private static long time = 3000; //自动播放时间


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex = (currentIndex + 1) % adapter.getCount();
            viewPager.setCurrentItem(currentIndex);
            play();
        }
    };

    public AutoScrollViewPager(ViewPagerFrameLayout viewPagerFrameLayout, PagerAdapter adapter) {
        this.viewPagerFrameLayout = viewPagerFrameLayout;
        this.adapter = adapter;
        this.viewPager = viewPagerFrameLayout.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        this.dotLayout = viewPagerFrameLayout.findViewById(R.id.dot_layout);
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
        viewPagerFrameLayout.setIndicatorDot(adapter.getCount());
    }

    /**
     * 播放，根据autoplay
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

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
        viewPager.setAdapter(adapter);
        initViewPager();
    }

    public void notifyDataSetChanged() {
        dotLayout.removeAllViews();
        viewPagerFrameLayout.setIndicatorDot(adapter.getCount());
        oldPosition = currentIndex = 0;
        adapter.notifyDataSetChanged();
    }

    public ViewPagerFrameLayout getViewPagerFrameLayout() {
        return viewPagerFrameLayout;
    }
}
