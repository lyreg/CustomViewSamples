package com.uestc.lyreg.customview.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Administrator on 2016/6/23.
 *
 * @Author lyreg
 */
public class Network {

    private static String BASE_URL = "http://gank.io/api/";

    private static OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private static Converter.Factory gsonConverterFacotry = GsonConverterFactory.create();
    private static Converter.Factory jacksonConverterFacotry = JacksonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFacatory = RxJavaCallAdapterFactory.create();

    private static GankFuliApi mGankFuliApi;

    public static GankFuliApi getGankFuliApi() {
        if(mGankFuliApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(jacksonConverterFacotry)
                    .addCallAdapterFactory(rxJavaCallAdapterFacatory)
                    .build();
            mGankFuliApi = retrofit.create(GankFuliApi.class);
        }

        return mGankFuliApi;
    }
}
