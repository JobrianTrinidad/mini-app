package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Route(value = "service-schedule", layout = MainLayout.class)
public class VehicleServiceScheduleView extends StandardFormView implements HasUrlParameter<String> {
    GridViewParameter gridViewParameter;
    int nSelectedEntityId = -1;

    public VehicleServiceScheduleView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleServiceSchedule.class, "");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onSelectEvent(ev -> {
                nSelectedEntityId = ev.getRow();
            });
            onAddEvent(ev -> {
                form.onNewItem((GuiItem) ev.getItem());
                this.setMessageStatus("This is new added value " + ((GuiItem) ev.getItem()).getRecordData().get(1));
            });

            onUpdateEvent(ev -> {
                int count;
                try {
                    count = form.onUpdateItem(new Object[]{ev.getRow(), ev.getColName(), ev.getColValue()});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (count > 0)
                    this.setMessageStatus(count + " rows is updated.");
            });

            onDeleteEvent(ev -> {
                int count;
                try {
                    count = form.onDeleteItemChecked();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (count > 0)
                    this.setMessageStatus(count + " rows is deleted.");
            });
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"),
                event.getRouteParameters().getInteger("___url_parameter"));

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

    private void createWorkshopJob() {
        ZJTVehicleServiceSchedule serviceSchedule =
                (ZJTVehicleServiceSchedule) repository
                        .findEntityById(ZJTVehicleServiceSchedule.class, this.nSelectedEntityId);
        if (serviceSchedule != null) {
            ZJTVehicleServiceJob entityServiceJob = new ZJTVehicleServiceJob();
            entityServiceJob.setPerformedDate(LocalDateTime.now());
            entityServiceJob.setComplete(false);
            entityServiceJob.setVehicle(serviceSchedule.getVehicle());
            entityServiceJob = (ZJTVehicleServiceJob) form.onNewItem(entityServiceJob, -1);

            ZJTVehicleServiceJobServiceType entityServiceType = new ZJTVehicleServiceJobServiceType();
            entityServiceType
                    .setVehicleServiceJob
                            (entityServiceJob);
            entityServiceType.setServiceType(serviceSchedule.getServiceType());
            form.onNewItem(entityServiceType, -1);

            ZJTVehicleServiceJobTask entityServiceJobTask = new ZJTVehicleServiceJobTask();
            entityServiceJobTask.setVehicleServiceJob(entityServiceJob);
            entityServiceJobTask.setComplete(false);
            form.onNewItem(entityServiceJobTask, -1);
            this.setMessageStatus("Workshop job created.");
        } else
            this.setMessageStatus("Please select row.");
    }

    private void completeWorkshopJob() {
        this.setMessageStatus("Workshop job complete.");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {

    }
}