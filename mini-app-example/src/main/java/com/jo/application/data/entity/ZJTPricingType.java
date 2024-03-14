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
@Table(name = "zjt_pricingtype")
@PageTitle("Pricing Type")
public class ZJTPricingType implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_pricingtype_id")
    private int zjt_pricingtype_id;

    @Column(name = "name")
    @ContentDisplayedInSelect
    @DisplayName(value = "Name")
    private String names = "";

    @Column
    @DisplayName(value = "Description")
    private String description = "";

    public int getZjt_pricingtype_id() {
        return zjt_pricingtype_id;
    }

    public void setZjt_pricingtype_id(int zjt_pricingtype_id) {
        this.zjt_pricingtype_id = zjt_pricingtype_id;
    }

    @Override
    public int getId() {
        return zjt_pricingtype_id;
    }

    public String getName() {
        return names;
    }

    public void setName(String name) {
        this.names = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
