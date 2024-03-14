package com.jo.application.views;

import com.jo.application.data.entity.ZJTVehicleServiceSchedule;
import com.jo.application.core.form.CommonForm;
import com.jo.application.core.form.GridViewParameter;
import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.ZJTVehicle;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "vehicle/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public VehicleView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicle.class, "");
        gridViewParameter.setSelectDefinition("fleetid");
        super.setGridViewParameter(gridViewParameter);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (this.form != null && this.isbGrid()) {
            CommonForm form =  this.form;
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
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

        if (parameter != null) {
            if (event.getRouteParameters().get("subcategory").isPresent()
                    && event.getRouteParameters().get("subcategory").get().equals("service-schedule")) {
                gridViewParameter.setEntityClass(ZJTVehicleServiceSchedule.class);
                gridViewParameter.setGroupClass(ZJTVehicle.class);
                gridViewParameter.setFilterClass(ZJTVehicle.class);
                gridViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
                TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter(new String[]{"vehicle.fleetid"}, "vehicle", new String[]{"planDate"}, null, null, "ZJTVehicleServiceSchedule");
                timeLineViewParameter.setGroupClass(ZJTVehicle.class);
                timeLineViewParameter.setSelectDefinition("fleetid");
                timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
                super.setTimeLineViewParameter(timeLineViewParameter);
            }
        } else
            addMenu(event.getRouteParameters().get("category"));
    }

    private void addMenu(Optional<String> category) {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);


        MenuItem editItem = contextMenu.addItem("Service Schedule");
        editItem.addContextMenuClickListener(e -> Notification.show(editItem.getCaption()));
        MenuItem gridItem = editItem.addSubItem("Grid");
        gridItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("vehicle/service-schedule/grid/" + e.getRow().get(0).getRowKey()));
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("vehicle/service-schedule/timeline/" + e.getRow().get(0).getRowKey()));

        this.setContextMenu(contextMenu);
    }

//    Override
//    void initWindow()
//    {
//        .getColumn("column1").setHeaderName("Column 1").setWidth("200px");
//        .getColumn("column2").setVisible(false);
//        .getColumn("column3").setReadonly(true);
//    }

}
