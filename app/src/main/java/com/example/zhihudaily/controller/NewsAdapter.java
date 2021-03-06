package com.example.zhihudaily.controller;

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
import com.example.zhihudaily.R;
import com.example.zhihudaily.util.Util;
import com.example.zhihudaily.model.entity.NewsWithDate;
import com.example.zhihudaily.model.entity.NormalNews;
import com.example.zhihudaily.model.entity.TopNews;
import com.example.zhihudaily.ui.ViewPagerFrameLayout;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NormalNews> newsList;
    private List<TopNews> topNewsList;
    private String todayDate;
    private ViewPagerFrameLayout.Controller controller;

    private static final int TYPE_NORMAL_NEWS = 0;
    private static final int TYPE_NEWS_WITH_DATE = 1;
    private static final int TYPE_VIEW_PAGER_FRAME = 2;

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
            View view = LayoutInflater.from(context).inflate(
                    R.layout.viewpagerframelayout, viewGroup, false);
            ViewPagerFrameViewHolder vh = new ViewPagerFrameViewHolder(view);
            PagerAdapter pagerAdapter = new ViewPagerAdapter(view.getContext(), topNewsList);
            controller = ((ViewPagerFrameLayout) view).getControllerInstance(pagerAdapter);
            return vh;
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
            controller.cancel();
            controller.play();
        } else {
            NormalNews news = newsList.get(i - 1);
            NewsViewHolder nvh = (NewsViewHolder) viewHolder;
            nvh.titleText.setText(news.getTitle());

            Glide.with(context)
                    .load(news.getImages().get(0))
                .placeholder(R.drawable.pic_error)
                .error(R.drawable.pic_error)
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

    public ViewPagerFrameLayout.Controller getViewPagerFrameLayoutController() {
        return controller;
    }
}

