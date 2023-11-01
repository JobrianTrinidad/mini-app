package com.aat.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "zjt_vehicle")
@PageTitle("TimeLine")
@AttributeOverride(name = "content", column = @Column(name = "title"))
public class ZJTVehicle extends ZJTSuperTimeLineNode implements Serializable {

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ZJTVehicleBooking> items;

    public List<ZJTVehicleBooking> getItems() {
        return items;
    }

    public void setItems(List<ZJTVehicleBooking> items) {
        this.items = items;
    }
}