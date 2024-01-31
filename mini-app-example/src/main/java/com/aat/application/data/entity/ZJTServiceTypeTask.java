package com.aat.application.data.entity;

import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Service kit is the holder for the BOM parts in Ampere
 */
@Entity
@Table(name = "zjt_servicetypetask")
@PageTitle("Service Type Task")
public class ZJTServiceTypeTask implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zjt_servicetypetask_id")
    protected int zjt_servicetypetask_id;

    @Column
    @DisplayName(value = "Seq No")
    private String seqno;

    @Column
    @DisplayName(value = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "zjt_vehicleservicetype_id")
    @DisplayName(value = "Service Type")
    private ZJTVehicleServiceType serviceType;

    @Override
    public int getId() {
        return zjt_servicetypetask_id;
    }

    public void setZjt_servicetypetask_id(int zjt_servicetypetask_id) {
        this.zjt_servicetypetask_id = zjt_servicetypetask_id;
    }

    public ZJTVehicleServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ZJTVehicleServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}