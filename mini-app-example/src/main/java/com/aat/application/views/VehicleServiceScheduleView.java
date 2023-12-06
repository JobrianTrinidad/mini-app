package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;

@Route(value="serviceschedule")
public class VehicleServiceScheduleView extends  StandardFormView<ZJTVehicleServiceSchedule> {

    public VehicleServiceScheduleView(BaseEntityRepository<ZJTVehicleServiceSchedule> repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);
    }
}