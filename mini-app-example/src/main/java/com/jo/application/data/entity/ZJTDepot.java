package com.jo.application.data.entity;

import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * Entity implementation class for Entity: ZJTUser
 */
@Entity
@Immutable
@Table(name = "`zjv_depot`")
@Subselect("select * from zjv_depot")
//@Table(name = "zjt_user")
@PageTitle("Depot")
public class ZJTDepot implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_depot_id")
    private int zjt_depot_id;

    @Column
    @ContentDisplayedInSelect(value = "name")
    @DisplayName(value = "Name")
    private String name = "";

    @Column
    @DisplayName(value = "Search Key")
    private String value = "";

    @Override
    public int getId() {
        return zjt_depot_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
