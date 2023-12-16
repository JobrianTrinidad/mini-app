package com.aat.application.data.entity;

import com.aat.application.annotations.ShowField;
import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "zjt_timeline_item")
public class ZJTTimeLineItem implements ZJTEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_timelineitem_id")
    private int zjt_timelineitem_id;
    @ManyToOne
    @JoinColumn(name = "node_id")
    @ShowField(show = true)
    private ZJTTimeLineNode group;

    public ZJTTimeLineNode getNode() {
        return group;
    }

    public void setNode(ZJTTimeLineNode group) {
        this.group = group;
    }

    @Override
    public int getId() {
        return zjt_timelineitem_id;
    }
}