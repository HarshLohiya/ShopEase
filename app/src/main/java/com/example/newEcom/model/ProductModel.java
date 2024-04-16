package com.example.newEcom.model;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private String name;
    private List<String> searchKey;
    private String image;
    private String category, description, specification;
    private int originalPrice, discount, price;
    private int productId, stock;
    private String shareLink;
    private float rating;
    private int noOfRating;

    public ProductModel() {
    }

    public ProductModel(String name, List<String> searchKey, String image, String category, String description, String specification, int originalPrice, int discount, int price, int productId, int stock, String shareLink, float rating, int noOfRating) {
        this.name = name;
        this.searchKey = searchKey;
        this.image = image;
        this.category = category;
        this.description = description;
        this.specification = specification;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.price = price;
        this.productId = productId;
        this.stock = stock;
        this.shareLink = shareLink;
        this.rating = rating;
        this.noOfRating = noOfRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(List<String> searchKey) {
        this.searchKey = searchKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(int noOfRating) {
        this.noOfRating = noOfRating;
    }
}
