package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "zjt_timeline_item")
public class ZJTTimeLineItem implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(name = "id")
    private Long id;

    @Column
    private String name;
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private ZJTTimeLineNode node;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZJTTimeLineNode getNode() {
        return node;
    }

    public void setNode(ZJTTimeLineNode node) {
        this.node = node;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}