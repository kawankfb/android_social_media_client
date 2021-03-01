package com.example.mvvm.network;
import com.example.mvvm.model.CategoryModel;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.PostModel;
import com.example.mvvm.model.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {
    @GET("discussion")
    Call<List<DiscussionModel>> getDiscussionList();


    @GET("followed_discussions")
    Call<List<DiscussionModel>> getFollowedDiscussionList();

    @GET("personal_discussions")
    Call<List<DiscussionModel>> getPersonalDiscussionList();

    @GET("me")
    Call<UserModel> getPersonalInformation();

    @GET("users/{user_id}")
    Call<UserModel> getUserInformation(@Path("user_id") int user_id);

    @GET("discussion/{discussion_id}")
    Call<DiscussionModel> getDiscussionInformation(@Path("discussion_id") int discussion_id);


    @GET("category")
    Call<List<CategoryModel>> getCategoriesList();


    @GET("posts/{discussion_id}")
    Call<List<PostModel>> getPostList(@Path("discussion_id") int discussion_id);

    @DELETE("discussion/{discussion_id}")
    Call<ResponseBody> deleteDiscussion(@Path("discussion_id") int discussion_id);

    @DELETE("posts/{post_id}")
    Call<ResponseBody> deletePost(@Path("post_id") int post_id);

    @POST("followed_discussions")
    Call<ResponseBody> addToFollowedDiscussion(@Body RequestBody requestBody);

    @DELETE("followed_discussions/{discussion_id}")
    Call<ResponseBody> removeFromFollowedDiscussions(@Path("discussion_id") int discussion_id);


    @POST("auth/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("auth/register")
    Call<ResponseBody> signup(@Body RequestBody requestBody);

    @POST("discussion")
    Call<ResponseBody> createDiscussion(@Body RequestBody requestBody);

    @POST("posts")
    Call<ResponseBody> createPost(@Body RequestBody requestBody);

    @POST("report")
    Call<ResponseBody> report(@Body RequestBody requestBody);

    @Multipart
    @POST("files")
    Call <ResponseBody>uploadFile(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name,
            @Part("setUserProfile") RequestBody setUserProfile,
            @Part("setDiscussionProfile") RequestBody setDiscussionProfile
    );

}
