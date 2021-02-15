package com.example.mvvm.model;

public class CategoryModel {
    String title;
    int id;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public CategoryModel(String title, int id) {
        this.title = title;
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }
}
