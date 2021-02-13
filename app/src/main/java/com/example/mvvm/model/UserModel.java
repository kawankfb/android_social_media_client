package com.example.mvvm.model;

import com.example.mvvm.network.RetrofitInstance;

public class UserModel {
    @Override
    public String toString() {
        return "UserModel{" +
                "created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", biography='" + biography + '\'' +
                ", id=" + id +
                '}';
    }

    public UserModel(String created_at, String updated_at, String email, String name, String profile, String biography, int id) {
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.biography = biography;
        this.id = id;
    }

    public void setUserModel(UserModel userModel) {
        this.created_at =   userModel.created_at;
        this.updated_at =   userModel.updated_at;
        this.email      =   userModel.email;
        this.name       =   userModel.name;
        this.profile    =   userModel.profile;
        this.biography  =   userModel.biography;
        this.id         =   userModel.id;
    }

    public String getProfilePreview(){
        return RetrofitInstance.BASE_URL+"profile_picture/"+id;
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

    public void setId(int id) {
        this.id = id;
    }


    String created_at;
    String updated_at;
    String email;
    String name;
    String profile;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public String getBiography() {
        return biography;
    }

    String biography;

    public UserModel() {
        id=-1;
    }

    public boolean isSet(){
        return id!=-1;
    }
    int id;

    public String getUserName() {
    return "anonymous";
    }
}
