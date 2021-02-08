package com.example.mvvm.model;

import com.example.mvvm.network.RetrofitInstance;

public class DiscussionModel {
    private String title;

    public int getDiscussion_id() {
        return id;
    }

    public void setDiscussion_id(int discussion_id) {
        this.id = discussion_id;
    }

    private int id;
    public String getUrl() {
        return RetrofitInstance.BASE_URL+url;
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

    public DiscussionModel(String title, String url,int id){
    this.title=title;
    this.url=url;
    this.id=id;
    }
}
