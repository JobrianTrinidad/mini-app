package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.MenuItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "pricing-type/:subcategory?/:filter?", layout = MainLayout.class)
public class PricingTypeView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public PricingTypeView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTPricingType.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onAddEvent(ev -> {
                ZJTEntity entity = null;
                if (subcategory.isPresent()) {
                    ZJTVehicleServiceJob serviceJob = (ZJTVehicleServiceJob) repository.findEntityById(ZJTVehicleServiceJob.class, urlParameter.orElse(-1));
                    switch (subcategory.get()) {
                        case "servicetype":
                            entity = new ZJTVehicleServiceJobServiceType();
                            ((ZJTVehicleServiceJobServiceType) entity).setVehicleServiceJob(serviceJob);
                            break;
                        case "task":
                            entity = new ZJTVehicleServiceJobTask();
                            ((ZJTVehicleServiceJobTask) entity).setVehicleServiceJob(serviceJob);
                            break;
                        case "servicekit":
                            entity = new ZJTVehicleServiceJobServiceKit();
                            ((ZJTVehicleServiceJobServiceKit) entity).setVehicleServiceJob(serviceJob);
                            break;
                        default:
                            break;
                    }
                } else
                    entity = new ZJTVehicleServiceJob();

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
//        if (parameter != null) {
//        } else
//            addMenu(event.getRouteParameters().get("category"));
    }

//    private void addMenu(Optional<String> category) {
//        AATContextMenu contextMenu = new AATContextMenu();
//        contextMenu.setOpenOnClick(true);
//        this.setContextMenu(contextMenu);
//    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"), event.getRouteParameters().getInteger("___url_parameter"));
    }


//    Override
//    void initWindow()
//    {
//        .getColumn("column1").setHeaderName("Column 1").setWidth("200px");
//        .getColumn("column2").setVisible(false);
//        .getColumn("column3").setReadonly(true);
//    }
}