package com.jo.application.data.entity;

import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;


/**
 * Entity implementation class for Entity: ZJTPricingType
 */
@Entity
@Table(name = "zjt_vehiclepart")
@PageTitle("Vehicle Parts")
public class ZJTVehiclePart implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_vehiclepart_id")
    private int zjt_vehiclepart_id;

    @Column
    @ContentDisplayedInSelect(value = "name")
    @DisplayName(value = "Name")
    private String name = "";

    @Column
    @DisplayName (value = "Description")
    private String description = "";

    @Column
    @DisplayName (value = "value")
    private String value = "";

    public int getZjt_vehiclepart_id() {
        return zjt_vehiclepart_id;
    }

    public void setZjt_vehiclepart_id(int zjt_vehiclepart_id) {
        this.zjt_vehiclepart_id = zjt_vehiclepart_id;
    }

    @Override
    public int getId() {
        return zjt_vehiclepart_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
