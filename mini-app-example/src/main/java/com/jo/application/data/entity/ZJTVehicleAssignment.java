package com.jo.application.data.entity;


import com.jo.application.annotations.DisplayName;
import com.jo.application.annotations.timeline.StartDate;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "zjt_vehicleassignment")
@PageTitle("Vehicle Assignment")
public class ZJTVehicleAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehicleassignment_id")
    private int zjt_vehicleassignment_id;


    @ManyToOne
    @JoinColumn(name = "zjt_vehicle_id")
    @DisplayName(value = "Vehicle")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "zjt_depot_id")
    @DisplayName(value = "Depot")
    private ZJTDepot depot;

    @Column(name = "startdate")
    @StartDate(className = "blue")
    @DisplayName(value = "Trip Start")
    private LocalDateTime startDate;

    @Column(name = "enddate")
    @DisplayName(value = "Trip End")
    private LocalDateTime endDate;

    @Column(name = "description")
    @DisplayName (value = "Description")
    private String description = "";

    //colorcode - background color of the content in timeline
    @Column(name = "colorcode")
    @DisplayName (value = "ColorCode")
    private String colorcode = "";

    @Column(name="isfinal")
    @DisplayName(value = "Final")
    private boolean isFinal;

    @Column(name="istripcomplete")
    @DisplayName(value = "Trip Complete")
    private boolean isTripComplete;

    //vehicle is in scheduled service - vehicle is automatically assigned
    @Column(name="inservice")
    @DisplayName(value = "In Service")
    private boolean inService;

    public int getId() {
        return zjt_vehicleassignment_id;
    }

    public ZJTVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ZJTVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZJTDepot getDepot() {
        return depot;
    }

    public void setDepot(ZJTDepot depot) {
        this.depot = depot;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isTripComplete() {
        return isTripComplete;
    }

    public void setTripComplete(boolean tripComplete) {
        isTripComplete = tripComplete;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }
}
