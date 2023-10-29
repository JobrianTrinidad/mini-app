package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import com.github.javaparser.quality.NotNull;
import jakarta.persistence.*;

import java.util.List;

@MappedSuperclass
public abstract class ZJTSuperTimeLineNode implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected int groupId;

    @Column(name = "name")
    protected String content;
    @Column(name = "level")
    protected int treeLevel = 0;


    protected boolean visible = true;
    protected String className = "";
    protected String nestedGroups;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Override
    public String getName() {
        return content;
    }

    @Override
    public void setName(String content) {
        this.content = content;
    }

    public String getNestedGroups() {
        return nestedGroups;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}