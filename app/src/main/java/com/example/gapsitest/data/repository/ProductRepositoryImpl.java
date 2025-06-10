package com.example.gapsitest.data.repository;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.gapsitest.data.model.SearchResponse;
import com.example.gapsitest.data.service.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductRepository {
    private static final String SEARCH_HISTORY_KEY = "search_history";
    private static final int MAX_HISTORY_ITEMS = 10;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    public ProductRepositoryImpl(ApiService apiService, SharedPreferences sharedPreferences) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void searchProducts(String query, int page, OnSearchCallback callback) {
        apiService.searchProducts(query, page, "best_match")
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Error en la respuesta del servidor");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    @Override

    public List<String> getSearchHistory() {
        String history = sharedPreferences.getString(SEARCH_HISTORY_KEY, "");
        return history.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(history.split(",")));
    }

    @Override
    public void saveSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) return;

        List<String> history = getSearchHistory();
        history.remove(query);
        history.add(0, query.trim());

        if (history.size() > MAX_HISTORY_ITEMS) {
            history = history.subList(0, MAX_HISTORY_ITEMS);
        }

        sharedPreferences.edit()
                .putString(SEARCH_HISTORY_KEY, TextUtils.join(",", history))
                .apply();
    }
}