package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.List;

@Entity
@Table(name = "zjt_timeline_node")
@PageTitle("TimeLine")
public class ZJTTimeLineNode implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int groupId;

    @Column(name = "name")
    private String content;
    @Column(name = "level")
    private int treeLevel;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ZJTTimeLineNode parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ZJTTimeLineNode> children;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ZJTTimeLineItem> items;
    private boolean visible = true;
    private String className = "";
    private String nestedGroups;

    // getters

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

    public List<ZJTTimeLineNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZJTTimeLineNode> children) {
        this.children = children;
    }

    public ZJTTimeLineNode getParent() {
        return parent;
    }

    // setters

    public void setParent(ZJTTimeLineNode parent) {
        this.parent = parent;
    }

    public List<ZJTTimeLineItem> getItems() {
        return items;
    }

    public void setItems(List<ZJTTimeLineItem> items) {
        this.items = items;
    }

    @Override
    public String getName() {
        return content;
    }

    @Override
    public void setName(String content) {
        this.content = content;
    }

    @PostLoad
    public void updateNestedGroups() {
        StringBuilder temp = new StringBuilder();
        if (children != null) {
            for (ZJTTimeLineNode node : children) {
                temp.append(node.getGroupId()).append(",");
            }
            if (!temp.isEmpty()) {
                temp.replace(temp.length() - 1, temp.length(), "");
            }
        }
        if (temp.toString().isEmpty())
            nestedGroups = null;
        else
            nestedGroups = temp.toString();
    }

    public String getNestedGroups() {
        return nestedGroups;
    }
}