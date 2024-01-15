package com.aat.application.data.entity;

import com.aat.application.core.data.entity.ZJTEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "zjt_driver")
@PageTitle("TimeLine")
public class ZJTDriver implements ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected int zjt_driver_id;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ZJTVehicleBooking> items;

    public List<ZJTVehicleBooking> getItems() {
        return items;
    }

    public void setItems(List<ZJTVehicleBooking> items) {
        this.items = items;
    }

    @Override
    public int getId() {
        return zjt_driver_id;
    }

    public void setZjt_driver_id(int zjt_driver_id) {
        this.zjt_driver_id = zjt_driver_id;
    }
}