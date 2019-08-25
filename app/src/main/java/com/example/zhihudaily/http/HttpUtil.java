package com.example.zhihudaily.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static OkHttpClient client = new OkHttpClient();

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
