package com.example.mvvm.model;

public class DiscussionModel {
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DiscussionModel(String title, String url){
    this.title=title;
    this.url=url;
    }
}
