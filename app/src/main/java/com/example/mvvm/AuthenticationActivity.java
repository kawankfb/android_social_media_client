package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationActivity extends AppCompatActivity  {
    EditText emailEditText;
    EditText passwordEditTet;
    public void login(View view){
String email=emailEditText.getText().toString();
String password=passwordEditTet.getText().toString();
String jsonString="{}";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("email",email);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString=jsonObject.toString();
        RequestBody requestBody =RequestBody.create(MediaType.parse("application/json"),jsonString);
        APIService apiService = RetrofitInstance.getNotAuthenticatedRetrofitClient().create(APIService.class);
        apiService.login(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(getContext(),"Entered credentials were invalid , error message : "+response.errorBody().string(),Toast.LENGTH_LONG).show();
                        return;
                    }
                    String json=response.body().string();
                    JsonElement jsonElement = new JsonParser().parse(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!jsonObject.has("access_token")){
                        Toast.makeText(getContext(),"Entered credentials were invalid , error message :",Toast.LENGTH_LONG).show();
                        return;
                    }
                    RetrofitInstance.setToken(jsonObject.get("access_token").getAsString());
                    Toast.makeText(getContext(),"Succesful Login",Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences=getContext().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.saved_access_token), jsonObject.get("access_token").getAsString());
                    editor.apply();
                    Intent intent=new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(getContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();
            return;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passwordEditTet=(EditText)findViewById(R.id.passwordEditText);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(getString(R.string.saved_access_token)))
        {
            String access_token=sharedPreferences.getString(getString(R.string.saved_access_token),"");
            if (access_token!="")
            {
                RetrofitInstance.setToken(access_token);
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }
    private Context getContext(){
        return this;
    }
}