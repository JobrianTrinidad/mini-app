package com.aat.application.views;

import com.aat.application.core.data.service.ZJTService;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.StandardForm;
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

import java.util.List;
import java.util.Optional;

@Route(value = "service-schedule/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleServiceScheduleView extends StandardFormView implements HasUrlParameter<String> {
    GridViewParameter gridViewParameter;
    int[] nSelectedEntityIds;

    public VehicleServiceScheduleView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleServiceSchedule.class, "");
        gridViewParameter.setDateFilterOn("planDate");
        gridViewParameter.setReadOnly(true);
        gridViewParameter.setAllowInsert(false);
        gridViewParameter.setAllowDelete(false);
        gridViewParameter.setFieldsAsReadOnly(new String[]{ "vehicle", "serviceType" });
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onItemMultiSelectEvent(ev -> {
                nSelectedEntityIds = ev.getRows();
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

        List<ZJTVehicleServiceSchedule> serviceSchedules =
                (List<ZJTVehicleServiceSchedule>) repository
                        .findEntitiesByIds(ZJTVehicleServiceSchedule.class, this.nSelectedEntityIds);
        int n = 0;
        if (serviceSchedules != null) {
            this.form.grid.setUnCheckAll();
            n = ZJTVehicleServiceSchedule.createWorkshopJob(repository, serviceSchedules);
            ((StandardForm<?, ?>) this.form).updateList();
            this.setMessageStatus(n + " workshop job created.");
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
                TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter(new String[]{"vehicleServiceJob.vehicle.fleetid"}, "vehicleServiceJob.vehicle", new String[]{"vehicleServiceJob.planDate"});
                timeLineViewParameter.setGroupClass(ZJTVehicleServiceSchedule.class);
                timeLineViewParameter.setSelectDefinition("vehicleServiceJob.vehicle.fleetid");
                timeLineViewParameter.setWhereDefinition("vehicleServiceJob.vehicle.zjt_vehicle_id");
                timeLineViewParameter.setGroupSelectDefinition("vehicle.fleetid");
                timeLineViewParameter.setGroupName("vehicleServiceJob");

                gridViewParameter.setFilterClass(ZJTVehicleServiceJob.class);
                gridViewParameter.setWhereDefinition("vehicleServiceJob.vehicle.zjt_vehicle_id");
                switch (event.getRouteParameters().get("subcategory").get()) {
                    case "service-job":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJob.class);
                        gridViewParameter.setFilterClass(ZJTVehicle.class);
                        gridViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
                        gridViewParameter.setGroupName("vehicle");
                        timeLineViewParameter.setTitleFieldName(new String[]{"vehicle.fleetid"});
                        timeLineViewParameter.setGroupIDFieldName("vehicle");
                        timeLineViewParameter.setStartDateFieldNames(new String[]{"planDate"});
                        timeLineViewParameter.setSelectDefinition("vehicle.fleetid");
//                        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
                        timeLineViewParameter.setWhereDefinitions(new String[]{"vehicle.zjt_vehicle_id", "serviceType.zjt_vehicleservicetype_id"});
//                        timeLineViewParameter.setSubcategory("service-job");

//                        timeLineViewParameter.setGroupClass(ZJTVehicleServiceJob.class);
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJob.class.getSimpleName());
                        timeLineViewParameter.setGroupName("vehicle");
                        break;
                    case "service-type":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobServiceType.class);
                        gridViewParameter.setGroupName("vehicleServiceJob");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobServiceType.class.getSimpleName());
                        break;
                    case "task":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobTask.class);
                        gridViewParameter.setGroupName("vehicleServiceJob");
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobTask.class.getSimpleName());
                        break;
                    case "service-kit":
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
//        serviceJobTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/service-job/timeline/" + e.getRow().get(0).getRowKey()));
        serviceJobTimeline.addContextMenuClickListener(e -> {
            String parameterVehicleId = "";
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    parameterVehicleId = cell.getCellValue();
                    break;
                }
            }
            String parameterServiceTypeId = "";
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("serviceType")) {
                    parameterServiceTypeId = cell.getCellValue();
                    break;
                }
            }
            UI.getCurrent().navigate("service-schedule/service-job/timeline/" + parameterVehicleId + "." + e.getRow().get(0).getRowKey() + "." + parameterServiceTypeId);
        });

        MenuItem serviceType = contextMenu.addItem("Service Job Type");
        MenuItem serviceTypeGrid = serviceType.addSubItem("Grid");
        serviceTypeGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/service-type/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem serviceTypeTimeline = serviceType.addSubItem("Timeline");
//        serviceTypeTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/service-type/timeline/" + e.getRow().get(0).getRowKey()));
        serviceTypeTimeline.addContextMenuClickListener(e -> {
            String parameterVehicleId = "";
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    parameterVehicleId = cell.getCellValue();
                    break;
                }
            }
            String parameterServiceTypeId = "";
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("serviceType")) {
                    parameterServiceTypeId = cell.getCellValue();
                    break;
                }
            }
            UI.getCurrent().navigate("service-schedule/service-type/timeline/" + parameterVehicleId + "." + e.getRow().get(0).getRowKey() + "." + parameterServiceTypeId);
        });

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
//        taskTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/task/timeline/" + e.getRow().get(0).getRowKey()));
        taskTimeline.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/task/timeline/" + cell.getCellValue());
                    break;
                }
            }
        });

        MenuItem serviceKit = contextMenu.addItem("ServiceJob & ServiceKit");
        MenuItem serviceKitGrid = serviceKit.addSubItem("Grid");
        serviceKitGrid.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/service-kit/grid/" + cell.getCellValue());
                    break;
                }
            }
        });
        MenuItem serviceKitTimeline = serviceKit.addSubItem("Timeline");
//        serviceKitTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-schedule/service-kit/timeline/" + e.getRow().get(0).getRowKey()));
        serviceKitTimeline.addContextMenuClickListener(e -> {
            for (Cell cell : e.getRow()) {
                if (cell.getColName().equals("vehicle")) {
                    UI.getCurrent().navigate("service-schedule/service-kit/timeline/" + cell.getCellValue());
                    break;
                }
            }
        });

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