package com.example.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.model.UserModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDiscussionInformationActivity extends AppCompatActivity {

    Toolbar toolbar;
    DiscussionModel discussionModel;
    int discussion_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discussion_information);
        toolbar=(Toolbar)findViewById(R.id.showDiscussionToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        discussion_id = getIntent().getIntExtra("EXTRA_DISCUSSION_ID",-1);
        if (discussion_id==-1)
        {
            onBackPressed();
            finish();
        }
        loadDiscussionInformation(discussion_id);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDiscussionInformation(int discussion_id) {
        APIService apiService = RetrofitInstance.getNotAuthenticatedRetrofitClient().create(APIService.class);
        Call<DiscussionModel> call=apiService.getDiscussionInformation(discussion_id);
        call.enqueue(new Callback<DiscussionModel>() {
            @Override
            public void onResponse(Call<DiscussionModel> call, Response<DiscussionModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    discussionModel=response.body();
                    ImageView discussion_profile_picture=(ImageView)findViewById(R.id.showDiscussionImageView);
                    TextView title=(TextView)findViewById(R.id.showDiscussionNameTextView);
                    TextView description=(TextView)findViewById(R.id.showDiscussionBiographyTextView);
                    TextView created_at=(TextView)findViewById(R.id.showDiscussionJoinTime);
                    title.setText(discussionModel.getTitle());
                    if (discussionModel.getDescription()==null || discussionModel.getDescription().length()<1){
                        description.setPadding(0,0,0,0);
                        description.setVisibility(View.INVISIBLE);
                    }else
                        description.setText(discussionModel.getDescription());
                    created_at.setText(discussionModel.getCreated_at());
                    Glide.with(getContext()).load(discussionModel.getId()).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(discussion_profile_picture);
                }
                else {

                    Toast.makeText(getContext(),"Please provide a valid user id",Toast.LENGTH_SHORT);
                    onBackPressed();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DiscussionModel> call, Throwable t) {
                Log.d("laravel_response","request failed");
                Toast.makeText(getContext(),"Please chack your internet connection",Toast.LENGTH_SHORT);
                onBackPressed();
                finish();
            }
        });
    }

    private Context getContext() {
    return this;
    }

    public void reportThisDiscussion(View view) {
        Intent intent =new Intent(this,ReportActivity.class);
        if (discussionModel!=null && discussionModel.getId()>0)
        intent.putExtra("EXTRA_USER_ID",discussionModel.getId());
        intent.putExtra("EXTRA_DISCUSSION_ID",discussion_id);
        intent.putExtra("EXTRA_REPORT_TYPE","user_report");
        startActivity(intent);
        finish();
    }
}