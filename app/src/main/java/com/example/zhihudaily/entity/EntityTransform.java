package com.example.zhihudaily.entity;

import java.util.List;

public class EntityTransform {

    /**
     * 获取DailyNews对象的NormalNews的集合，将集合中第一个NormalNews换成NewsWithDate。
     * @param dailyNews
     * @return
     */
    public static List<NormalNews> DailyNewsToNormalNews(DailyNews dailyNews) {
        List<NormalNews> normalNewsList =  dailyNews.getNormalNewsList();
        NormalNews normalNews = normalNewsList.get(0);
        NewsWithDate newsWithDate = new NewsWithDate();
        newsWithDate.setDate(dailyNews.getDate());
        newsWithDate.setType(normalNews.getType());
        newsWithDate.setId(normalNews.getId());
        newsWithDate.setTitle(normalNews.getTitle());
        newsWithDate.setGaPrefix(normalNews.getGaPrefix());
        newsWithDate.setImages(normalNews.getImages());
        normalNewsList.set(0, newsWithDate);
        return normalNewsList;
    }

    public static List<TopNews> DailyNewsToTopNews(DailyNews dailyNews) {
        return dailyNews.getTopNewsList();
    }
}
