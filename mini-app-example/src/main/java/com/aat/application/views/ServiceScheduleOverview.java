package com.aat.application.views;

import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.*;

@Route(value = "service-schedule-overview/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceScheduleOverview extends StandardFormView implements HasUrlParameter<String> {

    TimeLineViewParameter timeLineViewParameter;

    public ServiceScheduleOverview(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        timeLineViewParameter = new TimeLineViewParameter(new String[]{"vehicle.fleetid", "serviceType.name"}, "vehicle", "planDate", null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setGroupClass(ZJTVehicle.class);
        timeLineViewParameter.setDateFilterOn("planDate");
        timeLineViewParameter.setSelectDefinition("fleetid");
        super.setTimeLineViewParameter(timeLineViewParameter);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {

        } else {

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
    }
}