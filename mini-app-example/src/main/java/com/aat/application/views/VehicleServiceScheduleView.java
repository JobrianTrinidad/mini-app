package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.Cell;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Route(value = "service-schedule/:subcategory?/:filter?", layout = MainLayout.class)
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

    private void createWorkshopJob() {
        ZJTVehicleServiceSchedule serviceSchedule =
                (ZJTVehicleServiceSchedule) repository
                        .findEntityById(ZJTVehicleServiceSchedule.class, this.nSelectedEntityId);
        if (serviceSchedule != null) {
            String msg = serviceSchedule.createWorkshopJob(repository);
            if (msg != null) {
                this.setMessageStatus("Workshop job created.");
            } else {
                this.setMessageStatus(msg);
            }

        } else
            this.setMessageStatus("Please select row.");
    }

    private void completeWorkshopJob() {
        this.setMessageStatus("Workshop job complete.");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            if (event.getRouteParameters().get("subcategory").isPresent()) {
                TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter("vehicle.fleetid", "vehicle", "planDate");
                timeLineViewParameter.setGroupClass(ZJTVehicleServiceSchedule.class);
                timeLineViewParameter.setSelectDefinition("vehicle.fleetid");
                timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");

                gridViewParameter.setFilterClass(ZJTVehicleServiceJob.class);
                gridViewParameter.setWhereDefinition("vehicleServiceJob.vehicle.zjt_vehicle_id");
                switch (event.getRouteParameters().get("subcategory").get()) {
                    case "service-job":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJob.class);
                        gridViewParameter.setFilterClass(ZJTVehicle.class);
                        gridViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
                        gridViewParameter.setGroupName("vehicle");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJob.class.getSimpleName());
                        break;
                    case "servicetype":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobServiceType.class);
                        gridViewParameter.setGroupName("vehicleServiceJob");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobServiceType.class.getSimpleName());
                        break;
                    case "task":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobTask.class);
                        gridViewParameter.setGroupName("vehicleServiceJob");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobTask.class.getSimpleName());
                        break;
                    case "servicekit":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobServiceKit.class);
                        gridViewParameter.setGroupName("vehicleServiceJob");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobServiceKit.class.getSimpleName());
                        break;
                    default:
                        break;
                }
                super.setTimeLineViewParameter(timeLineViewParameter);
                gridViewParameter.setSelectDefinition("vehicle.fleetid");
                gridViewParameter.setGroupClass(ZJTVehicleServiceSchedule.class);

            }
        } else {
            addMenu(event.getRouteParameters().get("category"));
            gridViewParameter.setParameters(null);
        }
    }

    private void addMenu(Optional<String> category) {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);

        MenuItem serviceJob = contextMenu.addItem("Service Job");
        MenuItem serviceJobGrid = serviceJob.addSubItem("Grid");
        serviceJobGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/service-job/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem serviceJobTimeline = serviceJob.addSubItem("Timeline");
        serviceJobTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/service-job/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem serviceType = contextMenu.addItem("Service Job Type");
        MenuItem serviceTypeGrid = serviceType.addSubItem("Grid");
        serviceTypeGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/servicetype/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem serviceTypeTimeline = serviceType.addSubItem("Timeline");
        serviceTypeTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/servicetype/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem task = contextMenu.addItem("Service Job Task");
        MenuItem taskGrid = task.addSubItem("Grid");
        taskGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/task/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem taskTimeline = task.addSubItem("Timeline");
        taskTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/task/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem serviceKit = contextMenu.addItem("ServiceJob & ServiceKit");
        MenuItem serviceKitGrid = serviceKit.addSubItem("Grid");
        serviceKitGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/servicekit/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem serviceKitTimeline = serviceKit.addSubItem("Timeline");
        serviceKitTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/servicekit/timeline/" + e.getRow().get(0).getRowKey()));

        this.setContextMenu(contextMenu);
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
        if (event.getRouteParameters().get("subcategory").isEmpty()) {
            this.addCustomButton(button);
        }

        button = new Button();
        button.setIcon(new Icon(VaadinIcon.STOP_COG));
        button.setTooltipText("Complete current workshop job.");
        button.addClickListener(e -> completeWorkshopJob());
        if (event.getRouteParameters().get("subcategory").isEmpty()) {
            this.addCustomButton(button);
        }
    }
}