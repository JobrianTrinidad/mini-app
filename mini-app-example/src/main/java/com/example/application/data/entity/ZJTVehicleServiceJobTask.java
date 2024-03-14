package com.example.application.data.entity;

import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_vehicleservicejobtask")
@PageTitle("Service Job Task")
public class ZJTVehicleServiceJobTask implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_vehicleservicejobtask_id ")
    protected int zjt_vehicleservicejobtask_id;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicejob_id")
    @DisplayName(value = "Vehcle Service Job")
    private ZJTVehicleServiceJob vehicleServiceJob;

    @ManyToOne
    @JoinColumn(name = "zjt_servicetypetask_id")
    @DisplayName(value = "Service Task")
    private ZJTServiceTypeTask serviceTask;

    @Column
    @DisplayName(value = "Seq No")
    private int seqNo;

    @Column
    @DisplayName(value = "IsComplete")
    private boolean isComplete;

    @Override
    public int getId() {
        return zjt_vehicleservicejobtask_id;
    }

    public void setZjt_vehicleservicejobtask_id(int zjt_vehicleservicejobtask_id) {
        this.zjt_vehicleservicejobtask_id = zjt_vehicleservicejobtask_id;
    }

    public ZJTVehicleServiceJob getVehicleServiceJob() {
        return vehicleServiceJob;
    }

    public void setVehicleServiceJob(ZJTVehicleServiceJob vehicleServiceJob) {
        this.vehicleServiceJob = vehicleServiceJob;
    }

    public ZJTServiceTypeTask getServiceTask() {
        return serviceTask;
    }

    public void setServiceTask(ZJTServiceTypeTask serviceTask) {
        this.serviceTask = serviceTask;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}