package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm.adapter.DiscussionListAdapter;
import com.example.mvvm.adapter.PostListAdapter;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.PostModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.DiscussionListViewModel;
import com.example.mvvm.viewmodel.PostListViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements PostListAdapter.ItemListener {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    EditText postEditText;
    private int discussion_id;
    private List<PostModel> postModelList;
    private PostListAdapter adapter;
    private PostListViewModel viewModel;
    CountDownTimer countDownTimer;

    public void sendPost(View view) {
        String text = postEditText.getText().toString();
        String jsonString = "{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", text);
            jsonObject.put("discussion_id", discussion_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        apiService.createPost(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Something went wrong , error message : " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getContext(),"Successfully created.",Toast.LENGTH_LONG).show();
                    viewModel.makeApiCall(discussion_id);
                    postEditText.setText(null);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Context getContext() {
        return this;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        postEditText=(EditText)findViewById(R.id.postEditText);
        discussion_id= getIntent().getIntExtra("EXTRA_DISCUSSION_ID",0);
        RecyclerView postRecyclerView = (RecyclerView) findViewById(R.id.post_list_view);
        final TextView noPostFound = (TextView) findViewById(R.id.noPostFoundTextView);
        noPostFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        postRecyclerView.setLayoutManager(layoutManager);
        adapter = new PostListAdapter(this, postModelList, this);
        postRecyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(PostListViewModel.class);
        viewModel.getPostListObserver().observe(this, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(List<PostModel> postModels) {
                if (postModels != null) {
                    postModelList = postModels;
                    adapter.setPostList(postModels);
                    noPostFound.setVisibility(View.INVISIBLE);
                    postRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noPostFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall(discussion_id);
        countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                viewModel.makeApiCall(discussion_id);
                countDownTimer.start();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onPostClick(PostModel PostModel) {

    }

    @Override
    public void onPostHold(PostModel PostModel) {

    }
}