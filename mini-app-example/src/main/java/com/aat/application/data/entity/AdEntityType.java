package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ad_entitytype")
public class AdEntityType implements ZJTEntity {

    @Id
    @Column(name = "entitytype", nullable = false, length = 40)
    private String entityType;

    @Column(name = "ad_client_id", nullable = false)
    private int adClientId;

    @Column(name = "ad_org_id", nullable = false)
    private int adOrgId;

    @Column(name = "ad_entitytype_id", nullable = false)
    private int adEntityTypeId;

    @Column(name = "isactive", nullable = false, length = 1)
    private char isActive;

    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "createdby", nullable = false)
    private int createdBy;

    @Column(name = "updated", nullable = false)
    private Date updated;

    @Column(name = "updatedby", nullable = false)
    private int updatedBy;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "help", length = 2000)
    private String help;

    @Column(name = "version", length = 20)
    private String version;

    @Column(name = "modelpackage", length = 255)
    private String modelPackage;

    @Column(name = "classpath", length = 255)
    private String classPath;

    @Column(name = "processing", length = 1)
    private char processing;

    @Column(name = "ad_entitytype_uu", length = 36)
    private String adEntityTypeUu;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
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

    public int getAdEntityTypeId() {
        return adEntityTypeId;
    }

    public void setAdEntityTypeId(int adEntityTypeId) {
        this.adEntityTypeId = adEntityTypeId;
    }

    public char getIsActive() {
        return isActive;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public char getProcessing() {
        return processing;
    }

    public void setProcessing(char processing) {
        this.processing = processing;
    }

    public String getAdEntityTypeUu() {
        return adEntityTypeUu;
    }

    public void setAdEntityTypeUu(String adEntityTypeUu) {
        this.adEntityTypeUu = adEntityTypeUu;
    }

    @Override
    public int getId() {
        return adEntityTypeId;
    }
}

