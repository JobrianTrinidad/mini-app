package com.example.application.data.entity;

import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;


@Table(name = "ad_image")
@PageTitle("Image")
@Entity
public class ADImage implements ZJTEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_image_id")
    private int adImageId;

    @Column(name = "name", nullable = false)
    private String name;

    @javax.persistence.Lob
    @Column(name = "binarydata")
    private byte[] binaryData;

    @Column(name = "description")
    private String description;

    // Constructors

    // Getters and setters
    public int getAdImageId() {
        return adImageId;
    }

    public void setAdImageId(int adImageId) {
        this.adImageId = adImageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getId() {
        return adImageId;
    }
}

