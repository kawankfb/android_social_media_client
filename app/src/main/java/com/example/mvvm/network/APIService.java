package com.example.mvvm.network;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.PostModel;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("discussion")
    Call<List<DiscussionModel>> getDiscussionList();


    @GET("followed_discussions")
    Call<List<DiscussionModel>> getFollowedDiscussionList();

    @GET("personal_discussions")
    Call<List<DiscussionModel>> getPersonalDiscussionList();


    @GET("posts/{discussion_id}")
    Call<List<PostModel>> getPostList(@Path("discussion_id") int discussion_id);

    @POST("auth/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("discussion")
    Call<ResponseBody> createDiscussion(@Body RequestBody requestBody);

    @POST("posts")
    Call<ResponseBody> createPost(@Body RequestBody requestBody);
}
