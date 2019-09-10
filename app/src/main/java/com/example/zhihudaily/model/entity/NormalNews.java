package com.example.zhihudaily.model.entity;

import java.util.List;

public class NormalNews extends News {
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
