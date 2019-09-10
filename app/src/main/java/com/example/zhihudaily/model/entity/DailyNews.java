package com.example.zhihudaily.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyNews {
    private String date;

    @SerializedName("stories")
    private List<NormalNews> newsList;

    @SerializedName("top_stories")
    private List<TopNews> topNewsList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NormalNews> getNormalNewsList() {
        return newsList;
    }

    public void setNormalNewsList(List<NormalNews> newsList) {
        this.newsList = newsList;
    }

    public List<TopNews> getTopNewsList() {
        return topNewsList;
    }

    public void setTopNewsList(List<TopNews> topNewsList) {
        this.topNewsList = topNewsList;
    }
}
