package com.example.gapsitest.data.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private String id;
    private String title;
    private PriceInfo priceInfo;
    private ImageInfo imageInfo;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public PriceInfo getPriceInfo() { return priceInfo; }
    public void setPriceInfo(PriceInfo priceInfo) { this.priceInfo = priceInfo; }

    public ImageInfo getImageInfo() { return imageInfo; }
    public void setImageInfo(ImageInfo imageInfo) { this.imageInfo = imageInfo; }

    // Métodos de conveniencia
    public double getPrice() {
        return priceInfo != null ? priceInfo.currentPrice : 0.0;
    }

    public String getThumbnail() {
        return imageInfo != null ? imageInfo.thumbnailUrl : "";
    }

    public static class PriceInfo {
        public Double currentPrice;
    }

    public static class ImageInfo {
        public String thumbnailUrl;
    }
}