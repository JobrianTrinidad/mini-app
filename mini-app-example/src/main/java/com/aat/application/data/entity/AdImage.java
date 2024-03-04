package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ad_image")
@PageTitle("Image")
public class AdImage implements ZJTEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_image_id")
    private int adImageId;

    @Column(name = "ad_client_id", nullable = false)
    private int adClientId;

    @Column(name = "ad_org_id", nullable = false)
    private int adOrgId;

    @Column(name = "isactive", nullable = false)
    private Character isActive;

    @Column(name = "created", nullable = false)
    private Timestamp created;

    @Column(name = "createdby", nullable = false)
    private int createdBy;

    @Column(name = "updated", nullable = false)
    private Timestamp updated;

    @Column(name = "updatedby", nullable = false)
    private int updatedBy;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "binarydata")
    private byte[] binaryData;

    @Column(name = "entitytype", nullable = false)
    private String entityType;

    @Column(name = "description")
    private String description;

    @Column(name = "ad_image_uu")
    private String adImageUu;

    // Constructors, getters, and setters

    public int getAdImageId() {
        return adImageId;
    }

    public void setAdImageId(int adImageId) {
        this.adImageId = adImageId;
    }

    public int getAdClientId() {
        return adClientId;
    }

    public void setAdClientId(int adClientId) {
        this.adClientId = adClientId;
    }

    public int getAdOrgId() {
        return adOrgId;
    }

    public void setAdOrgId(int adOrgId) {
        this.adOrgId = adOrgId;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdImageUu() {
        return adImageUu;
    }

    public void setAdImageUu(String adImageUu) {
        this.adImageUu = adImageUu;
    }

    @Override
    public int getId() {
        return adImageId;
    }
}

