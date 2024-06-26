package com.jo.application.data.entity;

import com.jo.application.annotations.BaseItems;
import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "zjt_vehicle")
@PageTitle("Vehicle")
public class ZJTVehicle implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehicle_id")
    private int zjt_vehicle_id;

    @Column
    @DisplayName(value = "Chassis No")
    private String chassisno;

    @Column
    @DisplayName(value = "Description")
    private String description;

    @Column
    @DisplayName(value = "Disposal Date")
    private LocalDateTime disposaldate;

    @Column
    @DisplayName(value = "Engine Model")
    private String enginemodel;

    @Column
    @DisplayName(value = "Engine No")
    private String engineno;

    @Column
    @DisplayName(value = "Fuel Card Name")
    private String fuelcardname1;

    @Column
    @DisplayName(value = "Hire In")
    private boolean hirein;

    @Column
    @DisplayName(value = "Hub Meter")
    private Integer hubmeter;

    @Column
    @DisplayName(value = "Hub Meter Tele")
    private Integer hubmetertele;

    @Column
    @DisplayName(value = "KM Rate")
    private Integer kmrate;

    @Column
    @DisplayName(value = "KM Rate Days")
    private Integer kmratedays;

    @Column
    @DisplayName(value = "KM Rate Days Tele")
    private Integer kmratedaystele;

    @Column
    @DisplayName(value = "License")
    private String license;

    @Column
    @DisplayName(value = "Adult Seat")
    private Integer loadadultseat;

    @Column
    @DisplayName(value = "Make/Model")
    private String makemodel;

    @Column
    @DisplayName(value = "Operational")
    private boolean operational;

    @Column
    @DisplayName(value = "Purchase Date")
    private LocalDateTime purchasedate;

    @Column
    @DisplayName(value = "Vehicle ID")
    private String vehicleid;

    @Column
    @ContentDisplayedInSelect(value = "Fleet ID")
//    @NotNull
    @DisplayName(value = "Fleet ID")
    private String fleetid;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceSchedule> vehicleServiceSchedules;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleKMReading> kmReadings;

    @ManyToOne
    @JoinColumn(name = "zjt_depot_id")
    @DisplayName(value = "Depot")
    private ZJTDepot depot;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceJob> vehicleServiceJobs;

    public int getZjt_vehicle_id() {
        return zjt_vehicle_id;
    }

    @PrePersist
    public void prePersist() {
        if (this.disposaldate == null)
            this.disposaldate = LocalDateTime.now();
        if (this.purchasedate == null)
            this.purchasedate = LocalDateTime.now();
    }

    @Override
    public int getId() {
        return zjt_vehicle_id;
    }

    public void setZjt_vehicle_id(int zjt_vehicle_id) {
        this.zjt_vehicle_id = zjt_vehicle_id;
    }

    public String getChassisno() {
        return chassisno;
    }

    public void setChassisno(String chassisno) {
        this.chassisno = chassisno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDisposaldate() {
        return disposaldate;
    }

    public void setDisposaldate(LocalDateTime disposaldate) {
        this.disposaldate = disposaldate;
    }

    public String getEnginemodel() {
        return enginemodel;
    }

    public void setEnginemodel(String enginemodel) {
        this.enginemodel = enginemodel;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }

    public String getFuelcardname1() {
        return fuelcardname1;
    }

    public void setFuelcardname1(String fuelcardname1) {
        this.fuelcardname1 = fuelcardname1;
    }

    public boolean isHirein() {
        return hirein;
    }

    public void setHirein(boolean hirein) {
        this.hirein = hirein;
    }

    public Integer getHubmeter() {
        return hubmeter;
    }

    public void setHubmeter(Integer hubmeter) {
        this.hubmeter = hubmeter;
    }

    public Integer getHubmetertele() {
        return hubmetertele;
    }

    public void setHubmetertele(Integer hubmetertele) {
        this.hubmetertele = hubmetertele;
    }

    public Integer getKmrate() {
        return kmrate;
    }

    public void setKmrate(Integer kmrate) {
        this.kmrate = kmrate;
    }

    public Integer getKmratedays() {
        return kmratedays;
    }

    public void setKmratedays(Integer kmratedays) {
        this.kmratedays = kmratedays;
    }

    public Integer getKmratedaystele() {
        return kmratedaystele;
    }

    public void setKmratedaystele(Integer kmratedaystele) {
        this.kmratedaystele = kmratedaystele;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getLoadadultseat() {
        return loadadultseat;
    }

    public void setLoadadultseat(Integer loadadultseat) {
        this.loadadultseat = loadadultseat;
    }

    public String getMakemodel() {
        return makemodel;
    }

    public void setMakemodel(String makemodel) {
        this.makemodel = makemodel;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public LocalDateTime getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(LocalDateTime purchasedate) {
        this.purchasedate = purchasedate;
    }

    public String getFleetid() {
        return fleetid;
    }

    public void setFleetid(String fleetid) {
        this.fleetid = fleetid;
    }

    public ZJTDepot getDepot() {
        return depot;
    }

    public void setDepot(ZJTDepot depot) {
        this.depot = depot;
    }
}
