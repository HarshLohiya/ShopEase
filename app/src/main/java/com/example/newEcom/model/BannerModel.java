package com.example.newEcom.model;

public class BannerModel {
    int bannerId;
    String bannerImage, description, status;

    public BannerModel() {
    }

    public BannerModel(int bannerId, String bannerImage, String description, String status) {
        this.bannerId = bannerId;
        this.bannerImage = bannerImage;
        this.description = description;
        this.status = status;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
