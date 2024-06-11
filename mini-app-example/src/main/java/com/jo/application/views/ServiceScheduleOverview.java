package com.jo.application.views;

import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.ZJTVehicle;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.flow.router.*;

@Route(value = "service-schedule-overview/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceScheduleOverview extends StandardFormView implements HasUrlParameter<String> {

    TimeLineViewParameter timeLineViewParameter;

    public ServiceScheduleOverview(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        timeLineViewParameter = new TimeLineViewParameter(new String[]{"vehicle.fleetid", "serviceType.name"}
                , "vehicle", new String[]{"planDate", "dueDate"});
        timeLineViewParameter.setFromDefinition("ZJTVehicleServiceSchedule");
        timeLineViewParameter.setGroupClass(ZJTVehicle.class);
        timeLineViewParameter.setDateFilterOn("planDate");
        timeLineViewParameter.setSelectDefinition("fleetid");
        timeLineViewParameter.setGroupSelectDefinition("fleetid");
        timeLineViewParameter.setGroupCSSClass("fuelcardname1");
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