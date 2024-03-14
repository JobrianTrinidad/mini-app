package com.example.application.views;

import com.example.application.data.entity.ZJTServiceTypeKit;
import com.example.application.data.entity.ZJTServiceTypeTask;
import com.example.application.data.entity.ZJTVehicleServiceJob;
import com.example.application.data.entity.ZJTVehicleServiceType;
import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.form.GridViewParameter;
import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.views.StandardFormView;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "service-type/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceTypeView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public ServiceTypeView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleServiceType.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }


    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onAddEvent(ev -> {
                ZJTEntity entity = null;
                if (subcategory.isPresent()) {
                    ZJTVehicleServiceType serviceType = (ZJTVehicleServiceType) repository.findEntityById(ZJTVehicleServiceType.class, urlParameter.orElse(-1));
                    switch (subcategory.get()) {
                        case "service-type-kit":
                            entity = new ZJTServiceTypeKit();
                            ((ZJTServiceTypeKit) entity).setServiceType(serviceType);
                            break;
                        case "service-type-task":
                            entity = new ZJTServiceTypeTask();
                            ((ZJTServiceTypeTask) entity).setServiceType(serviceType);
                            break;
                        default:
                            break;
                    }
                } else
                    entity = new ZJTVehicleServiceType();

//                form.onNewItem((GuiItem) ev.getItem());
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
                timeLineViewParameter.setSelectDefinition("comments");
                timeLineViewParameter.setWhereDefinition("vehicleServiceJob.zjt_vehicleservicejob_id");

                switch (event.getRouteParameters().get("subcategory").get()) {
                    case "service-type-kit":
                        gridViewParameter.setEntityClass(ZJTServiceTypeKit.class);
                        timeLineViewParameter.setFromDefinition(ZJTServiceTypeKit.class.getSimpleName());
                        break;
                    case "service-type-task":
                        gridViewParameter.setEntityClass(ZJTServiceTypeTask.class);
                        timeLineViewParameter.setFromDefinition(ZJTServiceTypeTask.class.getSimpleName());
                        break;
                    default:
                        break;
                }
                super.setTimeLineViewParameter(timeLineViewParameter);
                gridViewParameter.setGroupClass(ZJTVehicleServiceType.class);
                gridViewParameter.setFilterClass(ZJTVehicleServiceType.class);
                gridViewParameter.setWhereDefinition("serviceType.zjt_vehicleservicetype_id");
            }
        } else
            addMenu(event.getRouteParameters().get("category"));
    }

    private void addMenu(Optional<String> category) {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);


        MenuItem editItem = contextMenu.addItem("Service Type Kit");
        editItem.addContextMenuClickListener(e -> Notification.show(editItem.getCaption()));
        MenuItem gridItem = editItem.addSubItem("Grid");
        gridItem.addContextMenuClickListener(e -> {
            UI.getCurrent().navigate("service-type/service-type-kit/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-type/service-type-kit/timeline/" + e.getRow().get(0).getRowKey()));

        MenuItem serviceTypeTaskItem = contextMenu.addItem("Service Type Task");
        serviceTypeTaskItem.addContextMenuClickListener(e -> Notification.show(serviceTypeTaskItem.getCaption()));
        MenuItem serviceTypeTaskGridItem = serviceTypeTaskItem.addSubItem("Grid");
        serviceTypeTaskGridItem.addContextMenuClickListener(e -> {
            UI.getCurrent().navigate("service-type/service-type-task/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem serviceTypeTaskTimeLineItem = serviceTypeTaskItem.addSubItem("Timeline");
        serviceTypeTaskTimeLineItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-type/service-type-task/timeline/" + e.getRow().get(0).getRowKey()));

        this.setContextMenu(contextMenu);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"), event.getRouteParameters().getInteger("___url_parameter"));
    }

}