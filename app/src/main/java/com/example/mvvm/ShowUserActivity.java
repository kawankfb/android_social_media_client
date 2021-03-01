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
import com.example.mvvm.model.UserModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    UserModel user;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        toolbar=(Toolbar)findViewById(R.id.showUserToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_id = getIntent().getIntExtra("EXTRA_USER_ID",-1);
        if (user_id==-1)
        {
            onBackPressed();
            finish();
        }
        loadUserInformation(user_id);

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

    private void loadUserInformation(int user_id){
        APIService apiService = RetrofitInstance.getNotAuthenticatedRetrofitClient().create(APIService.class);
        Call<UserModel> call=apiService.getUserInformation(user_id);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    user=response.body();
                    ImageView user_profile_picture=(ImageView)findViewById(R.id.showUserImageView);
                    TextView name=(TextView)findViewById(R.id.showUserNameTextView);
                    TextView biography=(TextView)findViewById(R.id.showUserBiographyTextView);
                    TextView joined_at=(TextView)findViewById(R.id.showUserJoinTime);
                    name.setText(user.getName());
                    if (user.getBiography()==null || user.getBiography().length()<1){
                        biography.setPadding(0,0,0,0);
                        biography.setVisibility(View.INVISIBLE);
                    }else
                    biography.setText(user.getBiography());
                    joined_at.setText(user.getCreated_at());
                        Glide.with(getContext()).load(user.getProfilePreview()).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(user_profile_picture);
                }
                else {

                    Toast.makeText(getContext(),"Please provide a valid user id",Toast.LENGTH_SHORT);
                    onBackPressed();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
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

    public void reportThisUser(View view) {

            Intent intent =new Intent(this,ReportActivity.class);
            intent.putExtra("EXTRA_USER_ID",user_id);
            intent.putExtra("EXTRA_REPORT_TYPE","user_report");
            startActivity(intent);
            finish();


    }
}