package com.aat.application.controller;

public class SaveImageResponse {
    private int savedImageId;
    private String message;

    // Constructor
    public SaveImageResponse(int savedImageId, String message) {
        this.savedImageId = savedImageId;
        this.message = message;
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
}

