package com.example.offer_java;

public class User {
    private String userId;
    private String name;
    private String lastMessage;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String name, String lastMessage) {
        this.userId = userId;
        this.name = name;
        if(lastMessage!= null){
            this.lastMessage = lastMessage;
        }
        else
            this.lastMessage = "Hello there";

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
}
