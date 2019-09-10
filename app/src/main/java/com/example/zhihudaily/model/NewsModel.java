package com.example.zhihudaily.model;

import java.io.IOException;

public interface NewsModel {
    void initNews(Callback callback);
    void loadMoreNews(String lastDayDate, Callback callback);

    interface Callback {
        void onSuccess(String s);
        void onFailure(IOException e);
    }
}
