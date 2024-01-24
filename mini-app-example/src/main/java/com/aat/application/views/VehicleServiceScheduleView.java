package com.aat.application.views;

import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

@Route(value = "service-schedule")
public class VehicleServiceScheduleView extends StandardFormView<ZJTVehicleServiceSchedule> {

    public VehicleServiceScheduleView(BaseEntityRepository<ZJTVehicleServiceSchedule> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        TimeLineViewParameter timeLineViewParameter =  new TimeLineViewParameter("timelineItemTitle", "vehicle", "planDate", null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
        super.setTimeLineViewParameter(timeLineViewParameter);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.START_COG));
        button.setTooltipText("Create workshop job.");
        button.addClickListener(e -> createWorkshopJob());
        this.addCustomButton(button);

        button = new Button();
        button.setIcon(new Icon(VaadinIcon.STOP_COG));
        button.setTooltipText("Complete current workshop job.");
        button.addClickListener(e -> completeWorkshopJob());
        this.addCustomButton(button);

    }

    private void createWorkshopJob()
    {
        this.setMessageStatus("Workshop job created.");
    }

    private  void completeWorkshopJob()
    {
        this.setMessageStatus("Workshop job complete.");
    }
}