package com.jo.application.data.entity;

import com.jo.application.annotations.DisplayName;
import com.jo.application.annotations.timeline.StartDate;
import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Column(name = "plandate")
    @StartDate(className = "red")
    @DisplayName(value = "Plan Date")
    private LocalDateTime planDate;
    @Column(name = "duedate")
    @StartDate(className = "blue")
    @DisplayName(value = "Due Date")
    private LocalDateTime dueDate;

    @Column
    @DisplayName(value = "In Progress")
    private Boolean inProgress;
    @Transient
    private ZJTItem item;

    public Boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public static int createWorkshopJob(BaseEntityRepository repository, List<ZJTVehicleServiceSchedule> serviceSchedules) {
        Map<Integer, ZJTVehicleServiceJob> vehicleJobs = new HashMap<Integer, ZJTVehicleServiceJob>();
        int n = 0;

        for (ZJTVehicleServiceSchedule serviceSchedule : serviceSchedules) {
            ZJTVehicleServiceType serviceType = serviceSchedule.getServiceType();
            ZJTVehicle vehicle = serviceSchedule.getVehicle();

            String query = "SELECT p FROM " + ZJTVehicleServiceJob.class.getSimpleName()
                    + " AS p WHERE not p.isComplete AND p.vehicle.zjt_vehicle_id = :param0 AND EXISTS ("
                    + "SELECT q FROM " + ZJTVehicleServiceType.class.getSimpleName() + " AS q "
                    + "WHERE q.zjt_vehicleservicetype_id = :param1)";
            Object[] params = new Object[]{vehicle.getId(), serviceType.getId()};

            List<Object[]> vehicleServiceJobs =
                    repository.findEntityByQuery(query, params);

            //check if there is an open service job for the vehicle of this service type
            if (vehicleServiceJobs.isEmpty()) {

                ZJTVehicleServiceJob entityServiceJob = vehicleJobs.get(vehicle.getId());

                if (entityServiceJob == null) {
                    entityServiceJob = new ZJTVehicleServiceJob();
                    entityServiceJob.setPerformedDate(LocalDateTime.now());
                    entityServiceJob.setComplete(false);
                    entityServiceJob.setVehicle(vehicle);
                    entityServiceJob = (ZJTVehicleServiceJob) repository.addNewEntity(entityServiceJob);

                    vehicleJobs.put(vehicle.getId(), entityServiceJob);
                }

                ZJTVehicleServiceJobServiceType entityServiceType = new ZJTVehicleServiceJobServiceType();
                entityServiceType.setVehicleServiceJob(entityServiceJob);
                entityServiceType.setServiceType(serviceSchedule.getServiceType());
                repository.addNewEntity(entityServiceType);

                List<ZJTEntity> serviceTypeTaskList = repository.findEntitiesFilteredBy(serviceSchedule.getServiceType()
                        , "serviceType", ZJTServiceTypeTask.class);
//                List<ZJTEntity> serviceTypeTaskList = repository.findAll(ZJTServiceTypeTask.class);
                for (ZJTEntity serviceTypeTask : serviceTypeTaskList) {
                    ZJTVehicleServiceJobTask entityServiceJobTask = new ZJTVehicleServiceJobTask();
                    entityServiceJobTask.setVehicleServiceJob(entityServiceJob);
                    entityServiceJobTask.setServiceTask((ZJTServiceTypeTask) serviceTypeTask);
                    //TODO - should sum seqno of other service type
                    entityServiceJobTask.setSeqNo(((ZJTServiceTypeTask) serviceTypeTask).getSeqNo());
                    entityServiceJobTask.setComplete(false);
                    repository.addNewEntity(entityServiceJobTask);
                }

                List<ZJTEntity> serviceJobServiceKitList = repository.findEntitiesFilteredBy(serviceSchedule.getServiceType()
                        , "serviceType", ZJTServiceTypeKit.class);
                for (ZJTEntity serviceJobServiceKit : serviceJobServiceKitList) {
                    ZJTVehicleServiceJobServiceKit vehicleServiceJobServiceKit = new ZJTVehicleServiceJobServiceKit();
                    vehicleServiceJobServiceKit.setVehicleServiceJob(entityServiceJob);
                    vehicleServiceJobServiceKit.setServiceKit(((ZJTServiceTypeKit) serviceJobServiceKit).getServiceKit());
                    repository.addNewEntity(vehicleServiceJobServiceKit);
                }

                serviceSchedule.setInProgress(true);
                repository.updateEntity(serviceSchedule);
                n++;
            }
        }
        return n;
    }

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
}