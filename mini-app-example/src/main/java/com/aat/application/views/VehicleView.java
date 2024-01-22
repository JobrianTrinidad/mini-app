package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.form.GridCommonForm;
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
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

//@Route(value="vehicle")
@Route(value = "vehicle/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleView extends StandardFormView<ZJTEntity> implements HasUrlParameter<String> {

    private String name;

    public VehicleView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter("vehicle.fleetid", "vehicle", "planDate", null, null, "ZJTVehicleServiceSchedule");
        timeLineViewParameter.setWhereDefinition("vehicle.zjt_vehicle_id");
        super.setTimeLineViewParameter(timeLineViewParameter);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (this.form != null) {
            GridCommonForm<ZJTEntity> form = (GridCommonForm<ZJTEntity>) this.form;

            onAddEvent(ev -> {
                form.save((GuiItem) ev.getItem());
            });

            onUpdateEvent(ev -> {
                form.onUpdateItem(ev.getRow(), ev.getColName(), ev.getColValue());
            });

            onDeleteEvent(ev -> {
                form.deleteCheckedRow();
            });
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            this.name = parameter;
        } else
            addMenu(event.getRouteParameters().get("category"));
    }

    private void addMenu(Optional<String> category) {
        AATContextMenu contextMenu = new AATContextMenu();
        contextMenu.setOpenOnClick(true);


        MenuItem editItem = contextMenu.addItem("Service Schedule");
        editItem.addContextMenuClickListener(e -> Notification.show(editItem.getCaption()));
        MenuItem gridItem = editItem.addSubItem("Grid");
        gridItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate("vehicle/serviceschedule/grid/" + e.getRow().get(0).getRowKey());
        });
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", ZJTVehicleServiceSchedule.class.getName());
            UI.getCurrent().navigate("vehicle/serviceschedule/timeline/" + e.getRow().get(0).getRowKey());
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
