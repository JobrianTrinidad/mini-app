package com.aat.application.data.entity;

import com.aat.application.annotations.DisplayName;
import com.aat.application.annotations.StartDate;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Entity implementation class for Entity: ZJTvehicleserviceschedule
 */
@Entity
@Table(name = "zjt_vehicleserviceschedule")
@PageTitle("Vehicle Service Schedule")
public class ZJTVehicleServiceSchedule implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehicleserviceschedule_id")
    private int zjt_vehicleserviceschedule_id;


    @ManyToOne
    @JoinColumn(name = "zjt_vehicle_id")
    @DisplayName(value = "Vehicle")
    private ZJTVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicetype_id")
    @DisplayName(value = "Service Type")
    private ZJTVehicleServiceType serviceType;

    @Column
    @DisplayName(value = "Last Service (KM)")
    private Integer lastServiceKM;

    @Column
    @StartDate(className = "bg-success")
    private LocalDateTime planDate;
    @Column
    @StartDate(className = "bg-warning")
    private LocalDateTime actualDate;

    public int getZjt_vehicleserviceschedule_id() {
        return zjt_vehicleserviceschedule_id;
    }

    public int getId() {
        return zjt_vehicleserviceschedule_id;
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

    public Integer getLastServiceKM() {
        return lastServiceKM;
    }

    public void setLastServiceKM(Integer lastServiceKM) {
        this.lastServiceKM = lastServiceKM;
    }

    public LocalDateTime getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDateTime planDate) {
        this.planDate = planDate;
    }

    public LocalDateTime getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDateTime actualDate) {
        this.actualDate = actualDate;
    }
}