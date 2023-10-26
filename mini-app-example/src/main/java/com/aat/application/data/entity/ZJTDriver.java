package com.aat.application.data.entity;

import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "zjt_driver")
@PageTitle("TimeLine")
public class ZJTDriver extends ZJTSuperTimeLineNode {
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<ZJTVehicleBooking> items;

    public List<ZJTVehicleBooking> getItems() {
        return items;
    }

    public void setItems(List<ZJTVehicleBooking> items) {
        this.items = items;
    }
}