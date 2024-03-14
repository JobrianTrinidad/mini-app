package com.jo.application.controller;

public class ImageResponse {
    private int savedImageId;
    private String message;
    private int status;

    // Constructor
    public ImageResponse(int savedImageId, String message, int status) {
        this.savedImageId = savedImageId;
        this.message = message;
        this.status = status;
    }

    // Getters and setters
    public int getSavedImageId() {
        return savedImageId;
    }

    public void setSavedImageId(int savedImageId) {
        this.savedImageId = savedImageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

