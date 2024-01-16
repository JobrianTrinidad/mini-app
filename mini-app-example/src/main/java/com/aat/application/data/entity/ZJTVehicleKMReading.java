package com.aat.application.data.entity;

import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Entity implementation class for Entity: ZJTvehicleKMReading
 */
@Entity
@Table(name = "zjt_vehiclekmreading")
@PageTitle("Vehicle KM Reading")
public class ZJTVehicleKMReading implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehiclekmreading_id")
    private int zjt_vehiclekmreading_id;


    @ManyToOne
    @JoinColumn(name = "zjt_vehicle_id")
    @DisplayName(value = "Vehicle")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "zjt_user_id")
    @DisplayName(value = "Logged by")
    private ZJTUser user;

    @Column
    @DisplayName(value = "KM Reading")
    private Integer KMreading;

    @Column
    @DisplayName(value = "Reading Date")
    private LocalDateTime readingDate;
    @Column
    @DisplayName(value = "Date Recorded")
    private LocalDateTime recordedDate;

    public int getZjt_vehiclekmreading_id() {
        return zjt_vehiclekmreading_id;
    }

    public int getId() {
        return zjt_vehiclekmreading_id;
    }

    public ZJTVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ZJTVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ZJTUser getUser() {
        return user;
    }

    public void setUser(ZJTUser user) {
        this.user = user;
    }

    public Integer getKMreading() {
        return KMreading;
    }

    public void setKMreading(Integer KMreading) {
        this.KMreading = KMreading;
    }

    public LocalDateTime getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDateTime readingDate) {
        this.readingDate = readingDate;
    }

    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
    }
}