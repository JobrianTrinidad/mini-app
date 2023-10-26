package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehicleBooking;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.Route;

import java.lang.reflect.InvocationTargetException;

@Route(value = "vehicle-timeline")
public class VehicleView extends TimeLineFormView<ZJTVehicleBooking> {
    public VehicleView(BaseEntityRepository<ZJTVehicleBooking> repository) {
        super(repository);
        super.setGroupName("vehicle");
        super.setGroupClass(ZJTVehicle.class);
    }
}