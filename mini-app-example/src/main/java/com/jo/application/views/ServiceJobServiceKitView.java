package com.jo.application.views;

import com.jo.application.core.form.GridViewParameter;
import com.jo.application.data.entity.ZJTVehicleServiceJobServiceKit;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "servicejob-servicekit/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceJobServiceKitView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public ServiceJobServiceKitView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleServiceJobServiceKit.class, "");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
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

        }
    }

//    private void addMenu(Optional<String> category) {
//        AATContextMenu contextMenu = new AATContextMenu();
//        contextMenu.setOpenOnClick(true);
//        this.setContextMenu(contextMenu);
//    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"),
                event.getRouteParameters().getInteger("___url_parameter"));
    }
}