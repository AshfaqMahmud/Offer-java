package com.example.offer_java;

import java.util.ArrayList;

public class User {
    private String userId;
    private String name;
    private String lastMessage;
    private String gender;
    private String phone;
    private String email;

    private String imageURL;
    private String imageDesc;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    // constructor for message
    public User(String userId, String name, String lastMessage) {
        this.userId = userId;
        this.name = name;
        if (lastMessage != null) {
            this.lastMessage = lastMessage;
        } else
            this.lastMessage = "Hello there";

    }

    // constructor for userinfo
    public User(String userId, String name, String gender, String phone) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    // constructor for image_data

    public User(String string, String string1) {
        this.imageURL=string;
        this.imageDesc=string1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }
}
