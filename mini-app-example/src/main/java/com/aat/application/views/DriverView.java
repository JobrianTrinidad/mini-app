package com.aat.application.views;

import com.aat.application.data.entity.ZJTDriver;
import com.aat.application.data.entity.ZJTTimeLineNode;
import com.aat.application.data.entity.ZJTVehicleBooking;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.repository.TimelineRepository;
import com.vaadin.flow.router.Route;

@Route(value = "driver-timeline")
public class DriverView extends TimeLineFormView {
    public DriverView(TimelineRepository repository) {
        super(repository);
//        super.setGroupName("driver");
//        super.setGroupClass(ZJTDriver.class);
    }
}