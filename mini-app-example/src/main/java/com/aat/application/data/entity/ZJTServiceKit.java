package com.aat.application.data.entity;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.util.List;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_servicekit")
@PageTitle("Service Kit")
public class ZJTServiceKit implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected int zjt_servicekit_id;

    @Override
    public int getId() {
        return zjt_servicekit_id;
    }

    public void setZjt_servicekit_id(int zjt_servicekit_id) {
        this.zjt_servicekit_id = zjt_servicekit_id;
    }

    @Column
    @ContentDisplayedInSelect(value = "name")
    @DisplayName(value = "Name")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}