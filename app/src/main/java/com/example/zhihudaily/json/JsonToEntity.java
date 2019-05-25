package com.example.zhihudaily.json;

import com.example.zhihudaily.entity.DailyNews;
import com.google.gson.Gson;

public class JsonToEntity {

//    public static DailyNews JsonToDailyNews(String json) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, DailyNews.class);
//    }

    public static <T> T jsonToEntity(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

}
