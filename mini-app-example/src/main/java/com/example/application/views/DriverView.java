package com.example.application.views;

import com.example.application.data.entity.ZJTVehicleServiceSchedule;
import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.views.StandardFormView;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.server.VaadinSession;

//@Route(value = "driver/:subcategory?/:filter?", layout = MainLayout.class)
public class DriverView<T extends ZJTEntity> extends StandardFormView implements HasUrlParameter<String> {
    private String name;

    public DriverView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        TimeLineViewParameter timeLineViewParameter =  new TimeLineViewParameter(new String[]{"timelineItemTitle"}, "vehicle", new String[]{"planDate"}, null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
        super.setTimeLineViewParameter(timeLineViewParameter);
        addMenu();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
//            this., new TimeLineViewParameter("timelineItemTitle", "planDate", null, null, "ZJTVehicleServiceSchedule")
            this.name = parameter;
        }
    }

    private void addMenu() {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);

        MenuItem editItem = contextMenu.addItem("Service Schedule");
        editItem.addContextMenuClickListener(e -> Notification.show(editItem.getCaption()));
        MenuItem gridItem = editItem.addSubItem("Grid");
        gridItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate("driver/serviceschedule/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate("driver/serviceschedule/timeline/" + e.getRow().get(0).getRowKey());
        });

        this.setContextMenu(contextMenu);
    }
}