package com.example.gapsitest.presenter;

import com.example.gapsitest.data.model.Product;
import com.example.gapsitest.data.model.SearchResponse;
import com.example.gapsitest.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View view;
    private ProductRepository repository;
    private String currentQuery = "";
    private int currentPage = 1;
    private int totalPages = 1;

    public ProductPresenter(ProductContract.View view, ProductRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override


    public void searchProducts(String query, boolean isNewSearch) {
        if (query.isEmpty()) return;

        currentQuery = query;
        if (isNewSearch) {
            currentPage = 1;
            view.showProgress(true);
            repository.saveSearchQuery(query);
            loadSearchHistory();
        }

        repository.searchProducts(query, currentPage, new ProductRepository.OnSearchCallback() {
            @Override
            public void onSuccess(SearchResponse response) {
                view.showProgress(false);

                List<Product> products = new ArrayList<>();

                // Verificar si hay resultados
                if (response.getItem() != null &&
                        response.getItem().getProps() != null &&
                        response.getItem().getProps().getPageProps() != null &&
                        response.getItem().getProps().getPageProps().getInitialData() != null &&
                        response.getItem().getProps().getPageProps().getInitialData().getSearchResult() != null) {

                    SearchResponse.SearchResult searchResult = response.getItem().getProps().getPageProps().getInitialData().getSearchResult();

                    if (searchResult.getItemStacks() != null) {
                        for (SearchResponse.ItemStack stack : searchResult.getItemStacks()) {
                            if (stack.getItems() != null) {
                                for (SearchResponse.ProductItem item : stack.getItems()) {
                                    Product product = new Product();
                                    product.setId(item.getId());
                                    product.setTitle(item.getTitle());

                                    Product.PriceInfo priceInfo = new Product.PriceInfo();
                                    priceInfo.currentPrice = item.getPriceInfo() != null ? item.getPriceInfo().getCurrentPrice() : 0.0;
                                    product.setPriceInfo(priceInfo);

                                    Product.ImageInfo imageInfo = new Product.ImageInfo();
                                    imageInfo.thumbnailUrl = item.getImageInfo() != null ? item.getImageInfo().getThumbnailUrl() : "";
                                    product.setImageInfo(imageInfo);

                                    products.add(product);
                                }
                            }
                        }
                    }
                }

                view.showProducts(products, isNewSearch);
            }

            @Override
            public void onError(String message) {
                view.showProgress(false);
                view.showError(message);
            }
        });
    }

    @Override
    public void loadMoreProducts() {
        if (currentPage < totalPages) {
            currentPage++;
            searchProducts(currentQuery, false);
        }
    }

    @Override
    public void loadSearchHistory() {
        view.updateSearchHistory(repository.getSearchHistory());
    }

    public List<String> getSearchHistory() {
        return repository.getSearchHistory();
    }




}
