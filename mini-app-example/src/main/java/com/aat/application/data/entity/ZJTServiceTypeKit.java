package com.aat.application.data.entity;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_servicetypekit")
@PageTitle("Service Kit")
public class ZJTServiceTypeKit implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_servicetypekit_id")
    protected int zjt_servicetypekit_id;

    @Override
    public int getId() {
        return zjt_servicetypekit_id;
    }

    public void setZjt_servicetypekit_id(int zjt_servicetypekit_id) {
        this.zjt_servicetypekit_id = zjt_servicetypekit_id;
    }


    @ManyToOne
    @JoinColumn(name = "zjt_servicekit_id")
    @DisplayName(value = "Service Kit")
    private ZJTServiceKit serviceKit;

    public ZJTServiceKit getServiceKit() {
        return serviceKit;
    }

    public void setServiceKit(ZJTServiceKit serviceKit) {
        this.serviceKit = serviceKit;
    }

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicetype_id")
    @DisplayName(value = "Service Type")
    private ZJTVehicleServiceType serviceType;

    public ZJTVehicleServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ZJTVehicleServiceType serviceType) {
        this.serviceType = serviceType;
    }


    @Column
    @DisplayName(value = "Qty")
    private BigDecimal qty;

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}