package com.aat.application.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "zjt_vehiclebooking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ZJTVehicleBooking extends ZJTSuperTimeLineItem implements Serializable {
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private ZJTDriver driver;

    public ZJTVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ZJTVehicle group) {
        this.vehicle = group;
    }

    public ZJTDriver getDriver() {
        return driver;
    }

    public void setDriver(ZJTDriver driver) {
        this.driver = driver;
    }

}