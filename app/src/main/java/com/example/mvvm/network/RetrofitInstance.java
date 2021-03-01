package com.example.mvvm.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL="http://185.231.115.253/api/";
    private static Retrofit retrofit;
    private static Retrofit fileRetrofit;
    private static Retrofit notAuthenticatedRetrofit;

    public static void setToken(String token) {
        RetrofitInstance.token = token;
    }

    private static String token;
    public static Retrofit getRetrofitClient() {
        token=getToken();
        if (retrofit==null)
        {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
            retrofit =new Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            return retrofit;
        }
        return retrofit;
    }
    public static Retrofit getFileRetrofitClient() {
        token=getToken();
        if (retrofit==null)
        {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60,TimeUnit.SECONDS).build();
            retrofit =new Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            return retrofit;
        }
        return retrofit;
    }

    private static String getToken() {
        return token;
    }

    public static Retrofit getNotAuthenticatedRetrofitClient() {
        if (notAuthenticatedRetrofit==null)
        {
            notAuthenticatedRetrofit =new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            return notAuthenticatedRetrofit;
        }
        return notAuthenticatedRetrofit;
    }

}
