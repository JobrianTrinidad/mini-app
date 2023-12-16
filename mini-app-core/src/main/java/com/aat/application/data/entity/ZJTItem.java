package com.aat.application.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "zjt_timeline")
public class ZJTItem {
    @Id
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column(name = "start")
    private LocalDateTime start;
    @Column(name = "end")
    private LocalDateTime end;
    @Column
    private String groupId;

    public ZJTItem() {
        this.start = LocalDateTime.now(); // Set start time to current time
        this.end = LocalDateTime.now(); // Set end time to current time
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}