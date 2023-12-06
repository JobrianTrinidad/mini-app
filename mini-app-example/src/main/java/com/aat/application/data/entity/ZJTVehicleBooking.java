package com.aat.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "zjt_vehiclebooking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ZJTVehicleBooking extends ZJTItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehiclebooking_id")
    private int zjt_vehiclebooking_id;
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

    @Override
    public int getId() {
        return zjt_vehiclebooking_id;
    }
}