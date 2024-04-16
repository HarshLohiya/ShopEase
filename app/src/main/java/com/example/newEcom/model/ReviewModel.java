package com.example.newEcom.model;

import com.google.firebase.Timestamp;

public class ReviewModel {
    String name;
    float rating;
    String title, review;
    Timestamp timestamp;

    public ReviewModel() {
    }

    public ReviewModel(String name, float rating, String title, String review, Timestamp timestamp) {
        this.name = name;
        this.rating = rating;
        this.title = title;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
