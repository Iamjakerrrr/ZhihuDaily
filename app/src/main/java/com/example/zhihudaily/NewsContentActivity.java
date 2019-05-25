package com.example.zhihudaily;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.entity.NewsContent;
import com.example.zhihudaily.http.HttpUtil;
import com.example.zhihudaily.json.JsonToEntity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class NewsContentActivity extends AppCompatActivity {
    public static final String NEWS_ID = "news_id";
    private static String REQUEST_NEWS_CONTENT_URL = "https://news-at.zhihu.com/api/4/news/";
    private NewsContent newsContent;
    private WebView webView;
    private ImageView largeImageView;
    private TextView newsTitleView;
    private RequestNewsCallback requestNewsCallback = new RequestNewsCallback();

    private class RequestNewsCallback {
        private okhttp3.Callback initNewsContent = new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    newsContent = JsonToEntity.jsonToEntity(res, NewsContent.class);
                    String css = newsContent.getCss().get(0);
                    String image = newsContent.getImage();
                    runOnUiThread(() -> {
                        Glide.with(NewsContentActivity.this)
                                .load(image)
//                .placeholder(R.mipmap.placeholder)
//                .error(R.mipmap.ic_launcher)
                                .into(largeImageView);
                        newsTitleView.setText(newsContent.getTitle());
                        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" " +
                                "href=\"" + css + "\" />" + newsContent.getBody();
                        webView.loadData(htmlData, "text/html", null);
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", Log.getStackTraceString(e));
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Toolbar toolbar = findViewById(R.id.news_content_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        webView = findViewById(R.id.webView);
        largeImageView = findViewById(R.id.large_image_view);
        newsTitleView = findViewById(R.id.news_title_text_view);
        Intent intent = getIntent();
        int newsId = intent.getIntExtra(NEWS_ID, -1);
        String url = REQUEST_NEWS_CONTENT_URL + newsId;
        HttpUtil.sendOkHttpRequest(url, requestNewsCallback.initNewsContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra(NEWS_ID, id);
        context.startActivity(intent);
    }
}
