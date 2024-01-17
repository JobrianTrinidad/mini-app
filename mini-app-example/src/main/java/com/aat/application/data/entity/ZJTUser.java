package com.aat.application.data.entity;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;


/**
 * Entity implementation class for Entity: ZJTUser
 */
@Entity
@Table(name = "zjt_user")
@PageTitle("User")
public class ZJTUser implements ZJTEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_user_id")
    private int zjt_user_id;

    @Column
    @DisplayName(value = "Name")
    @ContentDisplayedInSelect(value = "name")
    private String name = "";

    @Override
    public int getId() {
        return zjt_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
