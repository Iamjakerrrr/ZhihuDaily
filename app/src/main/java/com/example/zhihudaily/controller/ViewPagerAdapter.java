package com.example.zhihudaily.controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.model.entity.TopNews;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

//    private String[] resIds;
    private List<TopNews> topNewsList;
    private Context context;


    public ViewPagerAdapter(Context context, List<TopNews> topNewsList) {
        this.context = context;
        this.topNewsList = topNewsList;
    }

    @Override
    public int getCount() {
        return topNewsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.vp_item, null);
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.top_news_title_text_view);
        TopNews topNews = topNewsList.get(position);

        Glide.with(context)
                .load(topNewsList.get(position).getImage())
                .into(imageView);
        textView.setText(topNews.getTitle());
        //添加到容器中
        container.addView(view);

        /*
        view添加点击事件
         */
        view.setOnClickListener(v -> {
            NewsContentActivity.actionStart(context, topNews.getId());
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}