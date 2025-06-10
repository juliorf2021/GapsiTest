package com.example.gapsitest.data.service;

import com.example.gapsitest.data.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("wlm/walmart-search-by-keyword")
    Call<SearchResponse> searchProducts(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("sortBy") String sortBy
    );
}
