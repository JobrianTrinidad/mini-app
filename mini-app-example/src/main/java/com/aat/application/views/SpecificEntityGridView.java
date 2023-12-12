package com.aat.application.views;

import com.aat.application.data.entity.ZJTNode;
import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
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

//@Route(value="vehicle")
@Route(value = "view", layout = CoreMainLayout.class)
public class SpecificEntityGridView extends StandardFormView<ZJTNode> implements HasUrlParameter<String> {

    private String name;

    public SpecificEntityGridView(BaseEntityRepository<ZJTNode> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        addMenu();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            this.name = parameter;
        }
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
            Notification.show(e.getRow().get(12).getCellValue());
            VaadinSession.getCurrent().setAttribute("filteredEntityClass", this.entityClass.getName());
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            VaadinSession.getCurrent().setAttribute("filter", e.getRow());
            UI.getCurrent().navigate("view/" + this.name + "-grid");
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            VaadinSession.getCurrent().setAttribute("groupName", "vehicle");
            VaadinSession.getCurrent().setAttribute("groupClass", ZJTVehicle.class.getName());
            UI.getCurrent().navigate("view/timeline");
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
