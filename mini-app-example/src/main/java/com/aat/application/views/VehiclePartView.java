package com.aat.application.views;

import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;

@Route(value="vehicle-parts")
public class VehiclePartView extends  StandardFormView{

    public VehiclePartView(BaseEntityRepository repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);
        TimeLineViewParameter timeLineViewParameter =  new TimeLineViewParameter("timelineItemTitle", "vehicle", "planDate", null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
        super.setTimeLineViewParameter(timeLineViewParameter);

    }


}
