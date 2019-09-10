package com.example.zhihudaily.controller;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.example.zhihudaily.R;
import com.example.zhihudaily.model.NewsModel;
import com.example.zhihudaily.model.NewsModelImpl;
import com.example.zhihudaily.model.entity.DailyNews;
import com.example.zhihudaily.model.entity.EntityTransform;
import com.example.zhihudaily.model.entity.NormalNews;
import com.example.zhihudaily.model.entity.TopNews;
import com.example.zhihudaily.http.HttpUtil;
import com.example.zhihudaily.http.JsonToEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private List<TopNews> topNewsList;
    private List<NormalNews> normalNewsList;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ActionBar actionBar;
    private EndlessScrollListener endlessScrollListener;
    private String lastDayDate = "";
    private NewsModelImpl newsModelImpl = new NewsModelImpl();

    private NewsModel.Callback loadMoreNewsCallback = new NewsModel.Callback() {
        @Override
        public void onSuccess(String s) {
            DailyNews dailyNews = JsonToEntity.jsonToEntity(s, DailyNews.class);
            lastDayDate = dailyNews.getDate();
            normalNewsList.addAll(EntityTransform.DailyNewsToNormalNews(dailyNews));
            runOnUiThread(() -> {
                endlessScrollListener.setLoadState(EndlessScrollListener.LOADING_COMPLETE);
                newsAdapter.notifyDataSetChanged();
            });
        }

        @Override
        public void onFailure(IOException e) {

        }
    };

    private NewsModel.Callback initNewsCallback = new NewsModel.Callback() {
        @Override
        public void onSuccess(String s) {
            DailyNews dailyNews = JsonToEntity.jsonToEntity(s, DailyNews.class);
            lastDayDate = dailyNews.getDate();
            topNewsList.clear();
            topNewsList.addAll(EntityTransform.DailyNewsToTopNews(dailyNews));
            normalNewsList.clear();
            normalNewsList.addAll(EntityTransform.DailyNewsToNormalNews(dailyNews));
            runOnUiThread(() -> {
                newsAdapter.getViewPagerFrameLayoutController().notifyDataSetChanged();
                newsAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            });
        }

        @Override
        public void onFailure(IOException e) {
            runOnUiThread(() -> {
                swipeRefresh.setRefreshing(false);
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topNewsList = new ArrayList<>();
        normalNewsList = new ArrayList<>();

        Toolbar mainTooBar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(mainTooBar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        /*
        获取RecyclerView实例
         */
        RecyclerView newsRecyclerView = findViewById(R.id.news_recycler_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, normalNewsList, topNewsList);
        newsRecyclerView.setAdapter(newsAdapter);

        /*
        获取SwipeRefreshLayout实例
         */
        swipeRefresh = findViewById(R.id.swipe_refresh_layout);
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);
        swipeRefresh.setOnRefreshListener(() -> {
            initNews();
        });

        /*
        RecyclerView添加滑动监听
         */
        endlessScrollListener = new EndlessScrollListener() {

            @Override
            public void onLoadMore() {
                setLoadState(EndlessScrollListener.LOADING);
                /*
                获取今日之前的新闻列表
                 */
                newsModelImpl.loadMoreNews(lastDayDate, loadMoreNewsCallback);
            }
        };
        newsRecyclerView.addOnScrollListener(endlessScrollListener);

        initNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_tool_bar_menu, menu);
        return true;
    }

    private void initNews() {
        newsModelImpl.initNews(initNewsCallback);
    }
}