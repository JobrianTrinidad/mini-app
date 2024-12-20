package com.jo.application.data.entity;

import com.jo.application.core.data.entity.ZJTEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
//@Table(name = "zjt_timeline")
public class ZJTItem implements ZJTEntity {
    @Id
    private int id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private LocalDateTime startTime;
    @Column
    private int startDateId;
    @Column
    private LocalDateTime endTime;
    @Column
    private String groupId;
    @Column
    private String subgroupId;
    @Column
    private String className;

    @Column
    private String style;

    @Column
    private boolean editable = true;

    @Column
    private boolean selectable = true;

    public ZJTItem() {
        this.startTime = LocalDateTime.now(); // Set start time to current time
        this.endTime = LocalDateTime.now(); // Set end time to current time
    }

    public ZJTItem(String title, String groupId, LocalDateTime startTime, LocalDateTime endTime, String className) {
        this.title = title;
        this.groupId = groupId;
        this.startTime = startTime;
        this.endTime = (endTime != null) ? endTime : startTime.plusDays(1); // If endTime is null, set it to startTime plus 1 day
        this.className = className;
    }

    public ZJTItem(String title, String groupId, LocalDateTime startTime) {
        this.title = title;
        this.groupId = groupId;
        this.startTime = startTime;
        this.endTime = startTime.plusDays(1); // If endTime is null, set it to startTime plus 1 day
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getStartDateId() {
        return startDateId;
    }

    public void setStartDateId(int startDateId) {
        this.startDateId = startDateId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(String subgroupId) {
        this.subgroupId = subgroupId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}