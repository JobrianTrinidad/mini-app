package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "zjt_timeline_node")
@PageTitle("TimeLine")
public class ZJTTimeLineNode implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;
    @Column
    private int level;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ZJTTimeLineNode parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ZJTTimeLineNode> children;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private List<ZJTTimeLineItem> items;

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZJTTimeLineNode getParent() {
        return parent;
    }

    public List<ZJTTimeLineNode> getChildren() {
        return children;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setParent(ZJTTimeLineNode parent) {
        this.parent = parent;
    }

    public void setChildren(List<ZJTTimeLineNode> children) {
        this.children = children;
    }

    public List<ZJTTimeLineItem> getItems() {
        return items;
    }

    public void setItems(List<ZJTTimeLineItem> items) {
        this.items = items;
    }
}