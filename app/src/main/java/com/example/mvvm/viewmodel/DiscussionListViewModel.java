package com.example.mvvm.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.model.CategoryModel;
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

    public void makeApiCall(String tag){
        if (tag==null)
            getTrending();
        else if (tag.equals("followedDiscussions")) {
            getFollowed();
        }
        else if (tag.equals("categorizedDiscussions"))
        {

        }
        else if (tag.equals("personalDiscussions")){
        getPersonal();
        }
        else
            getTrending();
    }

    private void getFollowed() {
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<List<DiscussionModel>> call=apiService.getFollowedDiscussionList();
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
    private void getPersonal() {
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<List<DiscussionModel>> call=apiService.getPersonalDiscussionList();
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

    private void getTrending(){
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

    public void makeCategoryApiCall(CategoryModel categoryModel) {
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
