package com.example.offer_java;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    public String userId; // Unique identifier for the user
    public List<String> imageUrls; // List of image URLs associated with the user

    public UserData() {
        // Default constructor required for calls to DataSnapshot.getValue(UserData.class)
        imageUrls = new ArrayList<>();
    }

    public UserData(String userId, List<String> imageUrls) {
        this.userId = userId;
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
