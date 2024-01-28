package com.aat.application.views;

import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "service-schedule/:filter?", layout = MainLayout.class)
public class VehicleServiceScheduleView extends StandardFormView implements HasUrlParameter<String> {

    public VehicleServiceScheduleView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter("timelineItemTitle", "vehicle", "planDate", null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
        super.setTimeLineViewParameter(timeLineViewParameter);
        super.setGridViewParameter(new GridViewParameter(ZJTVehicleServiceSchedule.class, ""));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {

    }
}