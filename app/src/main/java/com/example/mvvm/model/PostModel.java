package com.example.mvvm.model;

import com.example.mvvm.network.RetrofitInstance;

public class PostModel {
    public PostModel(String created_at, String updated_at, String text, String file, int user_id, int id, int discussion_id) {
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.text = text;
        this.file = file;
        this.user_id = user_id;
        this.id = id;
        this.discussion_id = discussion_id;
    }
    public String getUrl(){
        return RetrofitInstance.BASE_URL+"profile_picture/"+user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(int discussion_id) {
        this.discussion_id = discussion_id;
    }

    String created_at;
    String updated_at;
    String text;
    String file;
    int user_id;
    int id;
    int discussion_id;

    public String getUserName() {
    return "anonymous";
    }
}
