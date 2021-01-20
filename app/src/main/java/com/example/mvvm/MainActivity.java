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
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.DiscussionListViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DiscussionListAdapter.ItemClickListener {

    private List<DiscussionModel> discussionModelList;
    private DiscussionListAdapter adapter;
    private DiscussionListViewModel viewModel;
    EditText titleEditText;
    CountDownTimer countDownTimer;


    public void createDiscussion(View view) {
        String title = titleEditText.getText().toString();
        String url = "http://kawankfb.ir/pictures/5.jpg";
        String jsonString = "{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        apiService.createDiscussion(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Something went wrong , error message : " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    viewModel.makeApiCall();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        RecyclerView discussionRecyclerView = (RecyclerView) findViewById(R.id.discussionlistview);
        final TextView noDiscussionFound = (TextView) findViewById(R.id.noDiscussionFoundTextView);
        noDiscussionFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        discussionRecyclerView.setLayoutManager(layoutManager);
        adapter = new DiscussionListAdapter(this, discussionModelList, this);
        discussionRecyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(DiscussionListViewModel.class);
        viewModel.getDiscussionListObserver().observe(this, new Observer<List<DiscussionModel>>() {
            @Override
            public void onChanged(List<DiscussionModel> discussionModels) {
                if (discussionModels != null) {
                    discussionModelList = discussionModels;
                    adapter.setDiscussionList(discussionModels);
                } else {
                    noDiscussionFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall();
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
    viewModel.makeApiCall();
    countDownTimer.start();
            }
        };
        countDownTimer.start();
    }
    private Context getContext() {
        return this;
    }

    @Override
    public void onDiscussionClick(DiscussionModel discussionModel) {
        Toast.makeText(this,"Clicked Discussion : "+discussionModel.getTitle(),Toast.LENGTH_LONG).show();
        viewModel.makeApiCall();
    }
}



