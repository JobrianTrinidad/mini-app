package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value="vehicle")
public class VehicleView extends  StandardFormView<ZJTVehicle> {

    public VehicleView(BaseEntityRepository<ZJTVehicle> repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);
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
            Notification.show(gridItem.getCaption());
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate(StandardFormView.class);
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            VaadinSession.getCurrent().setAttribute("groupName", "vehicle");
            VaadinSession.getCurrent().setAttribute("groupClass", ZJTVehicle.class.getName());
            UI.getCurrent().navigate(TimeLineFormView.class);
        });

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
