package com.example.mvvm.network;
import com.example.mvvm.model.DiscussionModel;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @GET("discussion")
    Call<List<DiscussionModel>> getDiscussionList();

    @POST("auth/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("discussion")
    Call<ResponseBody> createDiscussion(@Body RequestBody requestBody);
}
