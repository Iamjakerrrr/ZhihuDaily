package com.example.zhihudaily.entity;

import android.util.Log;

public class EntityTest {

    public static void printDailyNews(DailyNews dailyNews) {
        Log.d("entity", "dailyNews = null ? " + (dailyNews == null));
        if (dailyNews != null) {
            Log.d("entity", "date = " + dailyNews.getDate());
            for (NormalNews news : dailyNews.getNormalNewsList()) {
                printNormalNews(news);
            }
            for (TopNews news : dailyNews.getTopNewsList()) {
                printTopNews(news);
            }
            Log.d("entity", " ");
            Log.d("entity", " ");
        }
    }

    public static void printNormalNews(NormalNews news) {
        Log.d("entity", "news = null ? " + (news == null));
        if (news != null) {
            for (String s : news.getImages()) {
                Log.d("entity", "images = " + s);
            }
            Log.d("entity", "type = " + news.getType());
            Log.d("entity", "id = " + news.getId());
            Log.d("entity", "gaPrefix = " + news.getGaPrefix());
            Log.d("entity", "title = " + news.getTitle());
            Log.d("entity", " ");
            Log.d("entity", " ");
        }
    }

    public static void printTopNews(TopNews news) {
        Log.d("entity", "news = null ? " + (news == null));
        if (news != null) {
            Log.d("entity", "image = " + news.getImage());
            Log.d("entity", "type = " + news.getType());
            Log.d("entity", "id = " + news.getId());
            Log.d("entity", "gaPrefix = " + news.getGaPrefix());
            Log.d("entity", "title = " + news.getTitle());
            Log.d("entity", " ");
            Log.d("entity", " ");
        }
    }

    public static void printNewsWithDate(NewsWithDate newsWithDate) {
        Log.d("entity", "news = null ? " + (newsWithDate == null));
        if (newsWithDate != null) {
            Log.d("entity", "date = " + newsWithDate.getDate());
            for (String s : newsWithDate.getImages()) {
                Log.d("entity", "image = " + s);
            }
            Log.d("entity", "type = " + newsWithDate.getType());
            Log.d("entity", "id = " + newsWithDate.getId());
            Log.d("entity", "gaPrefix = " + newsWithDate.getGaPrefix());
            Log.d("entity", "title = " + newsWithDate.getTitle());
            Log.d("entity", " ");
            Log.d("entity", " ");
        }
    }

    public static void printNewsContent(NewsContent newsContent) {
        Log.d("entity", "newsContent = null ? " + (newsContent == null));
        if (newsContent != null) {
            Log.d("entity", "body's length = " + newsContent.getBody().length());
            Log.d("entity", "imageSource = " + newsContent.getImageSource());
            Log.d("entity", "title = " + newsContent.getTitle());
            Log.d("entity", "image = " + newsContent.getImage());
            Log.d("entity", "shareUrl = " + newsContent.getShareUrl());
            for (String s : newsContent.getJs()) {
                Log.d("entity", "js = " + s);
            }
            Log.d("entity", "gaPrefix = " + newsContent.getGaPrefix());
            Log.d("entity", "type = " + newsContent.getType());
            Log.d("entity", "id = " + newsContent.getId());
            for (String s : newsContent.getCss()) {
                Log.d("entity", "css = " + s);
            }
            Log.d("entity", " ");
            Log.d("entity", " ");
        }
    }
}
