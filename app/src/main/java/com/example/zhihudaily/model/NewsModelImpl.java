package com.example.zhihudaily.model;

import android.util.Log;
import com.example.zhihudaily.http.HttpUtil;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

public class NewsModelImpl implements NewsModel {
    private static final String todayNewsUrl = "https://news-at.zhihu.com/api/4/news/latest";
    private static final String moreNewsBaseUrl = "https://news-at.zhihu.com/api/4/news/before/";

    @Override
    public void initNews(Callback callback) {
        okhttp3.Callback initNewsCallback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", Log.getStackTraceString(e));
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    callback.onSuccess(res);
                } else {
                    response.close();
                }
            }
        };
        HttpUtil.sendOkHttpRequest(todayNewsUrl, initNewsCallback);
    }

    @Override
    public void loadMoreNews(String lastDayDate, Callback callback) {
        okhttp3.Callback loadMoreNewsCallback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", Log.getStackTraceString(e));
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    callback.onSuccess(res);
                } else {
                    response.close();
                }
            }
        };
        HttpUtil.sendOkHttpRequest(moreNewsBaseUrl + lastDayDate,
                loadMoreNewsCallback);
    }
}
