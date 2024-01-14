package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

//@Route(value = "driver/:subcategory?/:filter?", layout = MainLayout.class)
public class DriverView<T extends ZJTEntity> extends StandardFormView<ZJTEntity> implements HasUrlParameter<String> {
    private String name;

    public DriverView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService, new TimeLineViewParameter("timelineItemTitle", "", "planDate", null, null, "ZJTVehicleServiceSchedule"));
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