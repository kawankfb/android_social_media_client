package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDiscussionActivity extends AppCompatActivity {
    EditText titleEditText;

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
                    Toast.makeText(getContext(),"Successfully created.",Toast.LENGTH_LONG).show();
                    onBackPressed();
                    finish();
                    titleEditText.setText(null);
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
        setContentView(R.layout.activity_create_discussion);
        titleEditText=(EditText)findViewById(R.id.create_discussion_title);
    }

    public void cancelCreatingDiscussion(View view) {
        onBackPressed();
        finish();
    }
}