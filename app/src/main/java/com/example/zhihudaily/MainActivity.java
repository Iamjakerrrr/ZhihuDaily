package com.example.zhihudaily;

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

import com.example.zhihudaily.adapter.NewsAdapter;
import com.example.zhihudaily.entity.DailyNews;
import com.example.zhihudaily.entity.EntityTransform;
import com.example.zhihudaily.entity.NormalNews;
import com.example.zhihudaily.entity.TopNews;
import com.example.zhihudaily.http.HttpUtil;
import com.example.zhihudaily.json.JsonToEntity;

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
    private RequestNewsCallback requestNewsCallback = new RequestNewsCallback();

    private class RequestNewsCallback {
        private String lastDayDate = "";

        private okhttp3.Callback loadMoreNewsCallback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    DailyNews dailyNews = JsonToEntity.jsonToEntity(res, DailyNews.class);
                    lastDayDate = dailyNews.getDate();
                    normalNewsList.addAll(EntityTransform.DailyNewsToNormalNews(dailyNews));
                    runOnUiThread(() -> {
                        newsAdapter.setLoadState(NewsAdapter.LOADING_COMPLETE);
                        newsAdapter.notifyDataSetChanged();
                    });
                } else {
                    response.close();
                }
            }
        };

        private okhttp3.Callback initNewsCallback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    DailyNews dailyNews = JsonToEntity.jsonToEntity(res, DailyNews.class);
                    lastDayDate = dailyNews.getDate();
                    topNewsList.clear();
                    topNewsList.addAll(EntityTransform.DailyNewsToTopNews(dailyNews));
                    normalNewsList.clear();
                    normalNewsList.addAll(EntityTransform.DailyNewsToNormalNews(dailyNews));
                    runOnUiThread(() -> {
                        newsAdapter.getAutoScrollViewPager().notifyDataSetChanged();
                        newsAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    });
                } else {
                    response.close();
                }
            }
        };

    }


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
        newsRecyclerView.setNestedScrollingEnabled(false);

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
        newsRecyclerView.addOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore() {
                newsAdapter.setLoadState(NewsAdapter.LOADING);
                /*
                获取今日之前的新闻列表
                 */
                HttpUtil.sendOkHttpRequest("https://news-at.zhihu.com/api/4/news/before/" +
                                requestNewsCallback.lastDayDate, requestNewsCallback.loadMoreNewsCallback);
            }
        });
        initNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_tool_bar_menu, menu);
        return true;
    }

    private void initNews() {
        HttpUtil.sendOkHttpRequest("https://news-at.zhihu.com/api/4/news/latest",
                requestNewsCallback.initNewsCallback);
    }
}