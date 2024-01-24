package com.aat.application.data.entity;

import com.aat.application.annotations.BaseItems;
import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "servicetype", cascade = CascadeType.ALL)
    @BaseItems
    private List<ZJTServiceTypeKit> serviceTypeKits;
}
