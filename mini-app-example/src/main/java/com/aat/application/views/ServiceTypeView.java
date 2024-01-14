package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.entity.ZJTVehicleServiceType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "service-type", layout = CoreMainLayout.class)
public class ServiceTypeView extends StandardFormView<ZJTEntity> implements HasUrlParameter<String> {

    private String name;

    public ServiceTypeView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService, new TimeLineViewParameter("timelineItemTitle","", "planDate", null, null, "ZJTVehicleServiceSchedule"));
        this.getElement().getStyle().set("overflow-x", "hidden");
        addMenu();
    }

    private void addMenu() {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);

//        MenuItem fileItem = contextMenu.addItem("File");
//        fileItem.addContextMenuClickListener(e -> Notification.show(fileItem.getCaption()));

        MenuItem editItem = contextMenu.addItem("Service Schedule");
        editItem.addContextMenuClickListener(e -> Notification.show(editItem.getCaption()));
        MenuItem gridItem = editItem.addSubItem("Grid");
        gridItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("filteredEntityClass", this.entityClass.getName());
            VaadinSession.getCurrent().setAttribute("filter", e.getRow());
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate("service-type/serviceschedule");
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            VaadinSession.getCurrent().setAttribute("groupName", "serviceType");
            VaadinSession.getCurrent().setAttribute("groupClass", ZJTVehicleServiceType.class.getName());
            UI.getCurrent().navigate("timeline/service-type/serviceschedule");
        });

        this.setContextMenu(contextMenu);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            this.name = parameter;
        }
    }
}
