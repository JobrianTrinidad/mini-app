package com.aat.application.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "zjt_vehiclebooking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name = "findAllVehicle", query = "SELECT p FROM ZJTVehicle p")
@NamedQuery(name = "findAllDriver", query = "SELECT p FROM ZJTDriver p")
public class ZJTVehicleBooking extends ZJTSuperTimeLineItem implements Serializable {
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private ZJTDriver driver;

    @Column(name = "classname")
    private String className;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}