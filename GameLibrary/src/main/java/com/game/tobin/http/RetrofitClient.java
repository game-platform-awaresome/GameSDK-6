package com.game.tobin.http;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tobin on 2017/8/31.
 */

public class RetrofitClient {
    private static RetrofitClient ourInstance;

    private static int DEFAULT_TIMEOUT = 6000;

    public static RetrofitClient getInstance() {
        if( ourInstance == null){
            synchronized (RetrofitClient.class){
                if( ourInstance == null){
                    ourInstance = new RetrofitClient();
                }
            }
        }
        return ourInstance;
    }

    public Retrofit getBaseConfigRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gw.gamater.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(getOkHttpClient())
                .build();

        return  retrofit;
    }

    public Retrofit getLoginRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://login.gamater.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(getOkHttpClient())
                .build();

        return  retrofit;
    }

    OkHttpClient.Builder client = new OkHttpClient.Builder();

    private OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 使用拦截器
        httpClientBuilder.addInterceptor(new RequestInterceptor());
//        httpClientBuilder.addInterceptor(new LogInterceptor());
        return httpClientBuilder.build();
    }



}
