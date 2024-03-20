package com.jo.application.data.entity;

import com.jo.application.annotations.BaseItems;
import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Entity implementation class for Entity: ZJTvehicleservicetype
 */
@Entity
@Table(name = "zjt_vehicleservicetype")
@PageTitle("Service Type")
public class ZJTVehicleServiceType implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehicleservicetype_id")
    private int zjt_vehicleservicetype_id;


    @Column
    private String value = "";
    @Column(name = "name", insertable = false, updatable = false)
    @DisplayName(value = "Name")
    @ContentDisplayedInSelect(value = "Name")
    private String name = "";

    @Column
    @DisplayName(value = "Description")
    private String description = "";


    @Column
    @DisplayName(value = "Interval (KM)")
    private int km_interval;

    @Column
    @DisplayName(value = "Plan Date")
    private LocalDateTime planDate;

    @Column(name = "interval_type")
    @DisplayName (value = "Interval Type")
    @Enumerated(EnumType.STRING)
    private IntervalType intervalType = IntervalType.S;

    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTServiceTypeKit> serviceTypeKits;

    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTServiceTypeTask> zjtServiceTypeTasks;

    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTVehicleServiceJobServiceType> vehicleServiceJobServiceTypes;


    public int getZjt_vehicleservicetype_id() {
        return zjt_vehicleservicetype_id;
    }

    public void setZjt_vehicleservicetype_id(int zjt_vehicleservicetype_id) {
        this.zjt_vehicleservicetype_id = zjt_vehicleservicetype_id;
    }

    @Override
    public int getId() {
        return zjt_vehicleservicetype_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //    @Override
    public String getName() {
        return name;
    }

    //    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKm_interval() {
        return km_interval;
    }

    public void setKm_interval(int km_interval) {
        this.km_interval = km_interval;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
    }
}
