package com.example.gapsitest.di;



import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.gapsitest.data.repository.ProductRepository;
import com.example.gapsitest.data.repository.ProductRepositoryImpl;
import com.example.gapsitest.data.service.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppModule {

    public static SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("x-rapidapi-key", "KEY")
                            .header("x-rapidapi-host", "axesso-walmart-data-service.p.rapidapi.com")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    private static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://axesso-walmart-data-service.p.rapidapi.com/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService provideApiService() {
        return provideRetrofit().create(ApiService.class);
    }

    public static ProductRepository provideProductRepository(Context context) {
        return new ProductRepositoryImpl(
                provideApiService(),
                provideSharedPreferences(context)
        );
    }
}