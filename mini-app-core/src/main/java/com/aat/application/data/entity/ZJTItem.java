package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class ZJTItem implements ZJTEntity {
    //
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    protected int id;
    @Column(name = "name")
    protected String title;
    @Column(name = "description")
    protected String content;

    @Column(name = "start_time")
    protected LocalDateTime start;
    @Column(name = "end_time")
    protected LocalDateTime end;

    public ZJTItem() {
        this.start = LocalDateTime.now(); // Set start time to current time
        this.end = LocalDateTime.now(); // Set end time to current time
    }

//    @Override
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }


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