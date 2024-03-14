package com.example.application.data.entity;

import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

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
    private int seqNo;

    @Column
    @ContentDisplayedInSelect
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

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqno) {
        this.seqNo = seqno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}