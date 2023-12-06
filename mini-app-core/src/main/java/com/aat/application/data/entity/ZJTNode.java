package com.aat.application.data.entity;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@MappedSuperclass
public abstract class ZJTNode implements Serializable, ZJTEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    protected int groupId;

    @Column(name = "name")
    @ContentDisplayedInSelect(value = "content")
    protected String content;
    @Column(name = "level")
    protected int treeLevel = 0;

    @Transient
    protected boolean visible = true;
    @Column(name = "classname")
    protected String className = "";
    protected String nestedGroups;

//    public int getGroupId() {
//        return groupId;
//    }
//
//    @Override
//    public int getId() {
//        return groupId;
//    }
//
//    public void setGroupId(int groupId) {
//        this.groupId = groupId;
//    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}