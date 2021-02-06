package com.example.mvvm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    EditText signUpEmailEditText;
    EditText passwordEditText;
    EditText signUpPasswordEditText;
    EditText nameEditTet;
    EditText lastNameEditTet;
    EditText phoneNumberEditText;
    EditText userTagEditText;
    LinearLayout signUpLayout;
    LinearLayout loginLayout;
    TextView signUpTextView;
    TextView loginTextView;
    public void loadSignUpLayout(View view){
        loginLayout.setVisibility(View.INVISIBLE);
        signUpLayout.setVisibility(View.VISIBLE);
        loginTextView.setTextColor(getResources().getColor(R.color.textColor));
        signUpTextView.setTextColor(getResources().getColor(R.color.selectedTextview));
    }
    public void loadLoginLayout(View view){
        loginLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.INVISIBLE);
        loginTextView.setTextColor(getResources().getColor(R.color.selectedTextview));
        signUpTextView.setTextColor(getResources().getColor(R.color.textColor));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void pickDate(View view){
        view.setFocusable(false);
        DatePickerDialog dlg = new DatePickerDialog(this);
        dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("date of birth",String.format("%02d.%02d.%04d", dayOfMonth, month+1, year));
            }
        });
        dlg.show();
    }


    public void login(View view){
String email=emailEditText.getText().toString();
String password=passwordEditText.getText().toString();
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

            public void signUp(View view){
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();
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
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);

        signUpLayout=(LinearLayout)findViewById(R.id.signup_layout);
        loginLayout=(LinearLayout)findViewById(R.id.login_layout);
        loginTextView=(TextView)findViewById(R.id.login_text_view);
        signUpTextView=(TextView)findViewById(R.id.signup_textview);

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
                    if (passwordEditText.getText().length()>1 && emailEditText.getText().length()>1)
                        login(v);
                    return true;
                }
                return false;
            }
        });

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