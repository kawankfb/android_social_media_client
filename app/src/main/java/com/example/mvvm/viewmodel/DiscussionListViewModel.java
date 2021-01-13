package com.example.mvvm.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionListViewModel extends ViewModel {

    public MutableLiveData<List<DiscussionModel>> getDiscussionListObserver() {
        return discussionList;
    }

    private MutableLiveData<List<DiscussionModel>> discussionList;

    public DiscussionListViewModel() {
    discussionList=new MutableLiveData<>();
    }

    public void makeApiCall(){
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<List<DiscussionModel>> call=apiService.getDiscussionList();
        call.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {
                Log.d("laravel_response",response.toString());
discussionList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
                Log.d("laravel_response","request failed");
discussionList.postValue(null);
            }
        });
    }
}
