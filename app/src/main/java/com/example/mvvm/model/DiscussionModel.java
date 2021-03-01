package com.example.mvvm.model;

import com.example.mvvm.network.RetrofitInstance;

public class DiscussionModel {
    private String title;

    public DiscussionModel(String title, String description, String created_at, String updated_at, int id, String url) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.id = id;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getId() {
        return id;
    }

    private String description;
    private String created_at;
    private String updated_at;
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
