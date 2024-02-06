package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.CommonForm;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "service-kit/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceKitView extends StandardFormView implements HasUrlParameter<String> {
    GridViewParameter gridViewParameter;

    public ServiceKitView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTServiceKit.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            CommonForm form = this.form;
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
            if (event.getRouteParameters().get("subcategory").isPresent()) {
                TimeLineViewParameter timeLineViewParameter = new TimeLineViewParameter("serviceKit.name", "serviceKit", "planDate", null, null, "ZJTServiceKit");
                timeLineViewParameter.setGroupClass(ZJTServiceKit.class);
                timeLineViewParameter.setSelectDefinition("name");
                timeLineViewParameter.setWhereDefinition("serviceKit.zjt_servicekit_id");
                switch (event.getRouteParameters().get("subcategory").get()) {
                    case "service-type-kit":
                        gridViewParameter.setEntityClass(ZJTServiceTypeKit.class);
                        timeLineViewParameter.setFromDefinition(ZJTServiceTypeKit.class.getSimpleName());
                        break;
                    default:
                        break;
                }
                gridViewParameter.setGroupClass(ZJTServiceKit.class);
                gridViewParameter.setFilterClass(ZJTServiceKit.class);
                gridViewParameter.setWhereDefinition("serviceKit.zjt_servicekit_id");
                super.setTimeLineViewParameter(timeLineViewParameter);
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
        gridItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-kit/service-type-kit/grid/" + e.getRow().get(0).getRowKey()));
        MenuItem timelineItem = editItem.addSubItem("Timeline");
        timelineItem.addContextMenuClickListener(e -> UI.getCurrent().navigate("service-kit/service-type-kit/timeline/" + e.getRow().get(0).getRowKey()));

        this.setContextMenu(contextMenu);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"), event.getRouteParameters().getInteger("___url_parameter"));
    }


}
