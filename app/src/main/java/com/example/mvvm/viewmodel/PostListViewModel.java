package com.example.mvvm.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.model.PostModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListViewModel extends ViewModel {

    public MutableLiveData<List<PostModel>> getPostListObserver() {
        return postList;
    }

    private MutableLiveData<List<PostModel>> postList;

    public PostListViewModel() {
    postList=new MutableLiveData<>();
    }

    public void makeApiCall(int discussion_id){
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<List<PostModel>> call=apiService.getPostList(discussion_id);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                Log.d("laravel_post_response",response.toString());
postList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("laravel_post_response",t.toString());
postList.postValue(null);
            }
        });
    }
}
