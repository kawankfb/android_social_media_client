package com.example.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    Toolbar toolbar;
    int user_id;
    int post_id;
    int discussion_id;
    String report_type;
    EditText explanationEditText;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home :
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar=(Toolbar)findViewById(R.id.report_toolbar);
        explanationEditText=(EditText)findViewById(R.id.report_explanation_edit_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        report_type=getIntent().getStringExtra("EXTRA_REPORT_TYPE");
        discussion_id=getIntent().getIntExtra("EXTRA_DISCUSSION_ID",-1);
        user_id=getIntent().getIntExtra("EXTRA_USER_ID",-1);
        post_id= getIntent().getIntExtra("EXTRA_POST_ID",-1);
        if (report_type!=null){
            if (report_type.equals("discussion_report"))
                toolbar.setTitle("Report a Discussion");
            else if (report_type.equals("user_report"))
                toolbar.setTitle("Report a User");
            else if (report_type.equals("post_report"))
                toolbar.setTitle("Report a Post");
        }


    }

    public void cancelReport(View view) {
        onBackPressed();
        finish();
    }

    public void sendReport(View view) {
        String explanation=explanationEditText.getText().toString();
        if (explanation.length()<3){
            Toast.makeText(getContext(),"Please provide at least 3 characters",Toast.LENGTH_SHORT).show();
            return;}

        String jsonString="{}";
        JSONObject jsonObject=new JSONObject();
        try {
            if (user_id==-1)
            jsonObject.put("user_id",0);
            else
            jsonObject.put("user_id",user_id);

            jsonObject.put("explanation",explanation);
            if (discussion_id!=-1)
            jsonObject.put("discussion_id",discussion_id);
            if (post_id!=-1)
            jsonObject.put("post_id",post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString=jsonObject.toString();
        RequestBody requestBody =RequestBody.create(MediaType.parse("application/json"),jsonString);
        APIService apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        apiService.report(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(getContext(),response.errorBody().string(),Toast.LENGTH_LONG).show();
                        return;
                    }
                    String json=response.body().string();
                    JsonElement jsonElement = new JsonParser().parse(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!jsonObject.has("message")){
                        Toast.makeText(getContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getContext(),"Thank you for your report",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong with your internet connection please try again",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private Context getContext() {
        return this;
    }
}