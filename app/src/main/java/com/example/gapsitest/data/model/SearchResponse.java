package com.example.gapsitest.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResponse {
    @SerializedName("responseStatus")
    private String responseStatus;

    @SerializedName("responseMessage")
    private String responseMessage;

    @SerializedName("item")
    private ItemData item;

    @SerializedName("sortStrategy")
    private String sortStrategy;

    @SerializedName("domainCode")
    private String domainCode;

    @SerializedName("keyword")
    private String keyword;

    // Getters
    public String getResponseStatus() { return responseStatus; }
    public String getResponseMessage() { return responseMessage; }
    public ItemData getItem() { return item; }
    public String getSortStrategy() { return sortStrategy; }
    public String getDomainCode() { return domainCode; }
    public String getKeyword() { return keyword; }

    public static class ItemData {
        @SerializedName("assetPrefix")
        private String assetPrefix;

        @SerializedName("dynamicIds")
        private List<String> dynamicIds;

        @SerializedName("query")
        private Query query;

        @SerializedName("props")
        private Props props;

        public String getAssetPrefix() { return assetPrefix; }
        public List<String> getDynamicIds() { return dynamicIds; }
        public Query getQuery() { return query; }
        public Props getProps() { return props; }
    }

    public static class Query {
        @SerializedName("query")
        private String query;

        @SerializedName("page")
        private String page;

        @SerializedName("sort")
        private String sort;

        public String getQuery() { return query; }
        public String getPage() { return page; }
        public String getSort() { return sort; }
    }

    public static class Props {
        @SerializedName("pageProps")
        private PageProps pageProps;

        public PageProps getPageProps() { return pageProps; }
    }

    public static class PageProps {
        @SerializedName("initialData")
        private InitialData initialData;

        public InitialData getInitialData() { return initialData; }
    }

    public static class InitialData {
        @SerializedName("searchResult")
        private SearchResult searchResult;

        public SearchResult getSearchResult() { return searchResult; }
    }

    public static class SearchResult {
        @SerializedName("itemStacks")
        private List<ItemStack> itemStacks;

        @SerializedName("title")
        private String title;

        @SerializedName("aggregatedCount")
        private int aggregatedCount;

        public List<ItemStack> getItemStacks() { return itemStacks; }
        public String getTitle() { return title; }
        public int getAggregatedCount() { return aggregatedCount; }
    }

    public static class ItemStack {
        @SerializedName("items")
        private List<ProductItem> items;

        public List<ProductItem> getItems() { return items; }
    }

    public static class ProductItem {
        @SerializedName("usItemId")
        private String id;

        @SerializedName("name")
        private String title;

        @SerializedName("imageInfo")
        private ImageInfo imageInfo;

        @SerializedName("priceInfo")
        private PriceInfo priceInfo;

        public String getId() { return id; }
        public String getTitle() { return title; }
        public ImageInfo getImageInfo() { return imageInfo; }
        public PriceInfo getPriceInfo() { return priceInfo; }
    }

    public static class ImageInfo {
        @SerializedName("thumbnailUrl")
        private String thumbnailUrl;

        public String getThumbnailUrl() { return thumbnailUrl; }
    }

    public static class PriceInfo {
        @SerializedName("currentPrice")
        private double currentPrice;

        public double getCurrentPrice() { return currentPrice; }
    }
}