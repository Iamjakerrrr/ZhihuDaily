package com.example.zhihudaily.http;

import com.google.gson.Gson;

public class JsonToEntity {

    public static <T> T jsonToEntity(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

}
