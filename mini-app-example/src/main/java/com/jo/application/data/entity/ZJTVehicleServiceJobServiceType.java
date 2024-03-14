package com.jo.application.data.entity;

import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_vehicleservicejob_servicetype")
@PageTitle("Service Job & Service Type")
public class ZJTVehicleServiceJobServiceType implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_vehicleservicejob_servicetype_id ")
    protected int zjt_vehicleservicejob_servicetype_id;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicejob_id")
    @DisplayName(value = "Vehicle Service Job")
    private ZJTVehicleServiceJob vehicleServiceJob;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicetype_id")
    @DisplayName(value = "Service Type")
    private ZJTVehicleServiceType serviceType;


    @Override
    public int getId() {
        return zjt_vehicleservicejob_servicetype_id;
    }

    public void setZjt_vehicleservicejob_servicetype_id(int zjt_vehicleservicejob_servicetype_id) {
        this.zjt_vehicleservicejob_servicetype_id = zjt_vehicleservicejob_servicetype_id;
    }

    public ZJTVehicleServiceJob getVehicleServiceJob() {
        return vehicleServiceJob;
    }

    public void setVehicleServiceJob(ZJTVehicleServiceJob vehicleServiceJob) {
        this.vehicleServiceJob = vehicleServiceJob;
    }

    public ZJTVehicleServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ZJTVehicleServiceType serviceType) {
        this.serviceType = serviceType;
    }
}