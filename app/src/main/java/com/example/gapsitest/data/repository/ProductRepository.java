package com.example.gapsitest.data.repository;

import com.example.gapsitest.data.model.SearchResponse;

import java.util.List;

public interface ProductRepository {
    interface OnSearchCallback {
        void onSuccess(SearchResponse response);
        void onError(String message);
    }

    void searchProducts(String query, int page, OnSearchCallback callback);
    List<String> getSearchHistory();
    void saveSearchQuery(String query);
}