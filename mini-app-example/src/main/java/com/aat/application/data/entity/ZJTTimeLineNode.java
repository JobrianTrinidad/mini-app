package com.aat.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "zjt_timeline_node")
@NamedQuery(name = "findAllGroup", query = "SELECT p FROM ZJTTimeLineNode p")
@PageTitle("TimeLine")
public class ZJTTimeLineNode extends ZJTSuperTimeLineNode {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private ZJTTimeLineNode parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ZJTTimeLineNode> children;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ZJTTimeLineItem> items;

    public List<ZJTTimeLineNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZJTTimeLineNode> children) {
        this.children = children;
    }

    public ZJTTimeLineNode getParent() {
        return parent;
    }

    public void setParent(ZJTTimeLineNode parent) {
        this.parent = parent;
    }

    public List<ZJTTimeLineItem> getItems() {
        return items;
    }

    public void setItems(List<ZJTTimeLineItem> items) {
        this.items = items;
    }

    @PostLoad
    public void updateNestedGroups() {
        StringBuilder temp = new StringBuilder();
        if (children != null) {
            for (ZJTSuperTimeLineNode node : children) {
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
}