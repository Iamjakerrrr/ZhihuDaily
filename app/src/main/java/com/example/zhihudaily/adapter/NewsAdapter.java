package com.example.zhihudaily.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.NewsContentActivity;
import com.example.zhihudaily.R;
import com.example.zhihudaily.Util;
import com.example.zhihudaily.entity.NewsWithDate;
import com.example.zhihudaily.entity.NormalNews;
import com.example.zhihudaily.entity.TopNews;
import com.example.zhihudaily.ui.AutoScrollViewPager;
import com.example.zhihudaily.ui.ViewPagerFrameLayout;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NormalNews> newsList;
    private List<TopNews> topNewsList;
    private String todayDate;
    private AutoScrollViewPager autoScrollViewPager;

    private static final int TYPE_NORMAL_NEWS = 0;
    private static final int TYPE_NEWS_WITH_DATE = 1;
    private static final int TYPE_VIEW_PAGER_FRAME = 2;

    // 当前加载状态，默认为加载完成
    private int loadState = LOADING_COMPLETE;
    // 正在加载
    public static final int LOADING = 1;
    // 加载完成
    public static final int LOADING_COMPLETE = 2;

    static class BasicViewHolder extends RecyclerView.ViewHolder {
        View view;

        public BasicViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    static class ViewPagerFrameViewHolder extends BasicViewHolder {
        ViewPagerFrameLayout viewPagerFrameLayout;

        public ViewPagerFrameViewHolder(View view) {
            super(view);
            viewPagerFrameLayout = (ViewPagerFrameLayout) view;
        }
    }

    static class NewsViewHolder extends BasicViewHolder {
        TextView titleText;
        ImageView imageView;

        public NewsViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.news_title);
            imageView = view.findViewById(R.id.news_image);
        }
    }

    static class NewsWithDateViewHolder extends NewsViewHolder {
        TextView dateText;

        public NewsWithDateViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.date);
        }
    }

    public NewsAdapter(Context context, List<NormalNews> newsList, List<TopNews> topNewsList) {
        this.context = context;
        this.newsList = newsList;
        this.topNewsList = topNewsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_VIEW_PAGER_FRAME) {
            if (autoScrollViewPager == null) {
                View view = LayoutInflater.from(context).inflate(
                        R.layout.viewpagerframelayout, viewGroup, false);
                ViewPagerFrameViewHolder vh = new ViewPagerFrameViewHolder(view);
                PagerAdapter pagerAdapter = new ViewPagerAdapter(view.getContext(), topNewsList);
                autoScrollViewPager = new AutoScrollViewPager(
                            ((ViewPagerFrameViewHolder) vh).viewPagerFrameLayout, pagerAdapter);
                return vh;
            } else {
                ViewPagerFrameViewHolder vh = new ViewPagerFrameViewHolder(
                        autoScrollViewPager.getViewPagerFrameLayout());
                return vh;
            }
        } else if (i == TYPE_NORMAL_NEWS) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.news, viewGroup, false);
            NewsViewHolder vh = new NewsViewHolder(view);
            vh.view.setOnClickListener(v -> {
                int position = vh.getAdapterPosition() - 1;
                NormalNews news = newsList.get(position);
                NewsContentActivity.actionStart(context, news.getId());
            });
            return vh;
        } else {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.newswithdate, viewGroup, false);
            NewsWithDateViewHolder vh = new NewsWithDateViewHolder(view);
            vh.view.setOnClickListener(v -> {
                int position = vh.getAdapterPosition() - 1;
                NormalNews news = newsList.get(position);
                NewsContentActivity.actionStart(context, news.getId());
            });
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i == 0) {
            autoScrollViewPager.cancel();
            autoScrollViewPager.play();
        } else {
            NormalNews news = newsList.get(i - 1);
            NewsViewHolder nvh = (NewsViewHolder) viewHolder;
            nvh.titleText.setText(news.getTitle());

            Glide.with(context)
                    .load(news.getImages().get(0))
//                .placeholder(R.mipmap.placeholder)
//                .error(R.mipmap.ic_launcher)
                    .into(nvh.imageView);

            if (viewHolder.getClass() == NewsWithDateViewHolder.class) {
                NewsWithDateViewHolder nvh2 = (NewsWithDateViewHolder) viewHolder;
                NewsWithDate newsWithDate = (NewsWithDate) news;
                if (i - 1 == 0) {
                    todayDate = newsWithDate.getDate();
                }
                nvh2.dateText.setText(Util.formatDateString(todayDate, newsWithDate.getDate()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_VIEW_PAGER_FRAME;
        }
        if (newsList.get(position - 1).getClass() == NewsWithDate.class) {
            return TYPE_NEWS_WITH_DATE;
        }
        return TYPE_NORMAL_NEWS;
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    public int getLoadState() {
        return loadState;
    }

    public AutoScrollViewPager getAutoScrollViewPager() {
        return autoScrollViewPager;
    }
}

