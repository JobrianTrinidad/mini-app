package com.aat.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "zjt_timeline_item")
public class ZJTTimeLineItem extends ZJTSuperTimeLineItem {
    @ManyToOne
    @JoinColumn(name = "node_id")
    private ZJTTimeLineNode group;

    public ZJTTimeLineNode getNode() {
        return group;
    }

    public void setNode(ZJTTimeLineNode group) {
        this.group = group;
    }
}