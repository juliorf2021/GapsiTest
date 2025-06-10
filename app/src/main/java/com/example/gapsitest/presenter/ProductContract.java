package com.example.gapsitest.presenter;

import com.example.gapsitest.data.model.Product;

import java.util.List;

public interface ProductContract {
    interface View {
        void showProducts(List<Product> products, boolean isNewSearch);
        void showError(String message);
        void showProgress(boolean show);
        void updateSearchHistory(List<String> history);
    }

    interface Presenter {
        void searchProducts(String query, boolean isNewSearch);
        void loadMoreProducts();
        void loadSearchHistory();

        List<String> getSearchHistory();
    }
}
