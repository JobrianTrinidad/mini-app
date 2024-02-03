package com.aat.application.data.entity;

import com.aat.application.annotations.BaseItems;
import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_vehicleservicejob")
@PageTitle("Vehicle Service Job")
public class ZJTVehicleServiceJob implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_vehicleservicejob_id")
    protected int zjt_vehicleservicejob_id;

    @Column
    @DisplayName(value = "Comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "zjt_user_id")
    @DisplayName(value = "User")
    private ZJTUser performedBy;
    @Column
    @DisplayName(value = "KM Reading")
    private int kmReading;
    @Column
    @DisplayName(value = "Performed Date")
    private LocalDateTime performedDate;
    @Column
    @DisplayName(value = "IsComplete")
    private boolean isComplete;

    @ManyToOne
    @ContentDisplayedInSelect
    @JoinColumn(name = "zjt_vehicle_id")
    @DisplayName(value = "Vehicle")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicetype_id")
    @DisplayName(value = "Service Type")
    private ZJTVehicleServiceType serviceType;

    @OneToMany(mappedBy = "vehicleServiceJob", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceJobServiceType> vehicleServiceJobServiceTypes;

    @OneToMany(mappedBy = "vehicleServiceJob", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceJobTask> vehicleServiceJobTasks;
    @OneToMany(mappedBy = "vehicleServiceJob", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceJobServiceKit> vehicleServiceJobServiceKits;

    public int getZjt_vehicleservicejob_id() {
        return zjt_vehicleservicejob_id;
    }

    @Override
    public int getId() {
        return zjt_vehicleservicejob_id;
    }


    public void setZjt_vehicleservicejob_id(int zjt_vehicleservicejob_id) {
        this.zjt_vehicleservicejob_id = zjt_vehicleservicejob_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ZJTUser getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(ZJTUser user) {
        this.performedBy = user;
    }

    public int getKmReading() {
        return kmReading;
    }

    public void setKmReading(int kmReading) {
        this.kmReading = kmReading;
    }

    public LocalDateTime getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(LocalDateTime performedDate) {
        this.performedDate = performedDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public ZJTVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ZJTVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ZJTVehicleServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ZJTVehicleServiceType serviceType) {
        this.serviceType = serviceType;
    }
}