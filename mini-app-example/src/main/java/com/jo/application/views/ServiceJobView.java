package com.jo.application.views;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.form.GridViewParameter;
import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.ZJTVehicleServiceJob;
import com.jo.application.data.entity.ZJTVehicleServiceJobServiceKit;
import com.jo.application.data.entity.ZJTVehicleServiceJobServiceType;
import com.jo.application.data.entity.ZJTVehicleServiceJobTask;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "service-job/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceJobView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public ServiceJobView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleServiceJob.class, "");
        gridViewParameter.setSelectDefinition("vehicle.fleetid");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onAddEvent(ev -> {
                ZJTEntity entity = null;
                if (subcategory.isPresent()) {
                    ZJTVehicleServiceJob serviceJob = (ZJTVehicleServiceJob) repository.findEntityById(ZJTVehicleServiceJob.class, urlParameter.orElse(-1));
                    switch (subcategory.get()) {
                        case "servicetype":
                            entity = new ZJTVehicleServiceJobServiceType();
                            ((ZJTVehicleServiceJobServiceType) entity).setVehicleServiceJob(serviceJob);
                            break;
                        case "task":
                            entity = new ZJTVehicleServiceJobTask();
                            ((ZJTVehicleServiceJobTask) entity).setVehicleServiceJob(serviceJob);
                            break;
                        case "servicekit":
                            entity = new ZJTVehicleServiceJobServiceKit();
                            ((ZJTVehicleServiceJobServiceKit) entity).setVehicleServiceJob(serviceJob);
                            break;
                        default:
                            break;
                    }
                } else
                    entity = new ZJTVehicleServiceJob();

                form.onNewItem(entity, ((GuiItem) ev.getItem()).getId());
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
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            if (event.getRouteParameters().get("subcategory").isPresent()) {
                TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter(new String[]{"vehicleServiceJob.comments"}, "vehicleServiceJob", new String[]{"planDate"});
                timeLineViewParameter.setGroupClass(ZJTVehicleServiceJob.class);
                timeLineViewParameter.setSelectDefinition("vehicle.fleetid");
                timeLineViewParameter.setWhereDefinitions(new String[]{"vehicleServiceJob.zjt_vehicleservicejob_id"});

                switch (event.getRouteParameters().get("subcategory").get()) {
                    case "servicetype":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobServiceType.class);
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobServiceType.class.getSimpleName());
                        break;
                    case "task":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobTask.class);
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobTask.class.getSimpleName());
                        break;
                    case "servicekit":
                        gridViewParameter.setEntityClass(ZJTVehicleServiceJobServiceKit.class);
                        timeLineViewParameter.setFromDefinition(ZJTVehicleServiceJobServiceKit.class.getSimpleName());
                        break;
                    default:
                        break;
                }
                gridViewParameter.setGroupClass(ZJTVehicleServiceJob.class);
                gridViewParameter.setFilterClass(ZJTVehicleServiceJob.class);
                gridViewParameter.setWhereDefinition("vehicleServiceJob.zjt_vehicleservicejob_id");
                super.setTimeLineViewParameter(timeLineViewParameter);
            }
        } else
            addMenu(event.getRouteParameters().get("category"));
    }

    private void addMenu(Optional<String> category) {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);

        MenuItem serviceType = contextMenu.addItem("Service Type");
        serviceType.addContextMenuClickListener(e -> Notification.show(serviceType.getCaption()));
        MenuItem serviceTypeGrid = serviceType.addSubItem("Grid");
        serviceTypeGrid.addContextMenuClickListener(e -> {
            UI.getCurrent().navigate("service-job/servicetype/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem serviceTypeTimeline = serviceType.addSubItem("Timeline");
        serviceTypeTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-job/servicetype/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem task = contextMenu.addItem("Task");
        task.addContextMenuClickListener(e -> Notification.show(task.getCaption()));
        MenuItem taskGrid = task.addSubItem("Grid");
        taskGrid.addContextMenuClickListener(e -> {
            UI.getCurrent().navigate("service-job/task/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem taskTimeline = task.addSubItem("Timeline");
        taskTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-job/task/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem serviceKit = contextMenu.addItem("Service Kit");
        serviceKit.addContextMenuClickListener(e -> Notification.show(serviceKit.getCaption()));
        MenuItem serviceKitGrid = serviceKit.addSubItem("Grid");
        serviceKitGrid.addContextMenuClickListener(e -> {
            UI.getCurrent().navigate("service-job/servicekit/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem serviceKitTimeline = serviceKit.addSubItem("Timeline");
        serviceKitTimeline.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-job/servicekit/timeline/" + e.getRow().get(0).getRowKey()));

        this.setContextMenu(contextMenu);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"), event.getRouteParameters().getInteger("___url_parameter"));
    }

}