package com.aat.application.data.entity;

import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


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

    @Column (name = "plandate")
    @DisplayName(value = "Plan Date")
    private LocalDateTime planDate;
    @Column (name = "duedate")
    @DisplayName(value = "Due Date")
    private LocalDateTime dueDate;


    @Column
    @DisplayName(value = "In Progress")
    private boolean inProgress;
    @Transient
    private ZJTItem item;

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

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }


    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String createWorkshopJob(BaseEntityRepository repository)
    {

        //TODO - check if an open workshop job exists
        //return "Existing workshop job exists. Please complete it before creating new one."

        ZJTVehicleServiceJob entityServiceJob = new ZJTVehicleServiceJob();
        entityServiceJob.setPerformedDate(LocalDateTime.now());
        entityServiceJob.setComplete(false);
        entityServiceJob.setVehicle(this.getVehicle());
        entityServiceJob = (ZJTVehicleServiceJob)repository.addNewEntity(entityServiceJob);


        ZJTVehicleServiceJobServiceType entityServiceType = new ZJTVehicleServiceJobServiceType();
        entityServiceType
                .setVehicleServiceJob
                        (entityServiceJob);
        entityServiceType.setServiceType(this.getServiceType());
        repository.addNewEntity(entityServiceType);

        List<ZJTEntity> serviceTypeTaskList = repository.findAll(ZJTServiceTypeTask.class);
        for (ZJTEntity serviceTypeTask : serviceTypeTaskList) {
            ZJTVehicleServiceJobTask entityServiceJobTask = new ZJTVehicleServiceJobTask();
            entityServiceJobTask.setVehicleServiceJob(entityServiceJob);
            entityServiceJobTask.setServiceTask((ZJTServiceTypeTask) serviceTypeTask);
            entityServiceJobTask.setSeqNo(((ZJTServiceTypeTask) serviceTypeTask).getSeqNo());
            entityServiceJobTask.setComplete(false);
            repository.addNewEntity(entityServiceJobTask);
        }

        List<ZJTEntity> serviceJobServiceKitList = repository.findAll(ZJTServiceKit.class);
        for (ZJTEntity serviceJobServiceKit : serviceJobServiceKitList) {
            ZJTVehicleServiceJobServiceKit vehicleServiceJobServiceKit = new ZJTVehicleServiceJobServiceKit();
            vehicleServiceJobServiceKit.setVehicleServiceJob(entityServiceJob);
            vehicleServiceJobServiceKit.setServiceKit((ZJTServiceKit) serviceJobServiceKit);
            repository.addNewEntity(vehicleServiceJobServiceKit);
        }
        return null;

    }
}