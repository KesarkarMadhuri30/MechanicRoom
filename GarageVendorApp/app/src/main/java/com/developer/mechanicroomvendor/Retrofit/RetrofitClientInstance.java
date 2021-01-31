package com.developer.mechanicroomvendor.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static final String BASE_URL = "http://testapp.steponglobal.com/api/vehicle/";

    private static Retrofit retrofit = null;
    private static RetrofitClientInstance retrofitSingletonInstance;

    private RetrofitClientInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized RetrofitClientInstance getInstance() {
        if (retrofitSingletonInstance == null)
            retrofitSingletonInstance = new RetrofitClientInstance();
        return retrofitSingletonInstance;
    }

    public RetrofitApiClient getApi() {
        return retrofit.create(RetrofitApiClient.class);
    }
}
