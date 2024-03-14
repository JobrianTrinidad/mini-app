package com.jo.application.data.entity;

import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_vehicleservicejob_servicekit")
@PageTitle("Service Job & Service Kit")
public class ZJTVehicleServiceJobServiceKit implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_vehicleservicejob_servicekit_id  ")
    protected int zjt_vehicleservicejob_servicekit_id;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicejob_id")
    @DisplayName(value = "Vehicle Service Job")
    private ZJTVehicleServiceJob vehicleServiceJob;

    @ManyToOne
    @JoinColumn(name = "zjt_servicekit_id")
    @DisplayName(value = "Service Kit")
    private ZJTServiceKit serviceKit;

    @Column
    @DisplayName(value = "Plan Qty")
    private double planQty;

    @Column
    @DisplayName(value = "Actual Qty")
    private double actualQty;

    @Override
    public int getId() {
        return zjt_vehicleservicejob_servicekit_id;
    }

    public void setZjt_vehicleservicejob_servicekit_id(int zjt_vehicleservicejob_servicekit_id) {
        this.zjt_vehicleservicejob_servicekit_id = zjt_vehicleservicejob_servicekit_id;
    }

    public ZJTVehicleServiceJob getVehicleServiceJob() {
        return vehicleServiceJob;
    }

    public void setVehicleServiceJob(ZJTVehicleServiceJob vehicleServiceJob) {
        this.vehicleServiceJob = vehicleServiceJob;
    }

    public ZJTServiceKit getServiceKit() {
        return serviceKit;
    }

    public void setServiceKit(ZJTServiceKit serviceKit) {
        this.serviceKit = serviceKit;
    }

    public double getPlanQty() {
        return planQty;
    }

    public void setPlanQty(double planQty) {
        this.planQty = planQty;
    }

    public double getActualQty() {
        return actualQty;
    }

    public void setActualQty(double actualQty) {
        this.actualQty = actualQty;
    }
}