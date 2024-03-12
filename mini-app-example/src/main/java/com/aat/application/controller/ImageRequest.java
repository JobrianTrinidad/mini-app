package com.aat.application.controller;

import java.util.Base64;

public class ImageRequest {

    private String name;
    private String description;
    private byte[] imageData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageDataBase64(String imageDataBase64) {
        this.imageData = Base64.getDecoder().decode(imageDataBase64);
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImageData() {
        return imageData;
    }

}
