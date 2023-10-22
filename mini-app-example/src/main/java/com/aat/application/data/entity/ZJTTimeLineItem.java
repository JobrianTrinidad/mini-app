package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "zjt_timeline_item")
@NamedQuery(name = "findAllGroup", query = "SELECT p FROM ZJTTimeLineNode p")
public class ZJTTimeLineItem implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;
    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private ZJTTimeLineNode group;
    @Column(name = "start_time")
    private LocalDateTime start;
    @Column(name = "end_time")
    private LocalDateTime end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override

    public String getName() {
        return title;
    }
    @Override

    public void setName(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZJTTimeLineNode getNode() {
        return group;
    }

    public void setNode(ZJTTimeLineNode group) {
        this.group = group;
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public void setStartTime(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEndTime() {
        return end;
    }

    public void setEndTime(LocalDateTime end) {
        this.end = end;
    }
}