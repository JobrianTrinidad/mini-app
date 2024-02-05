package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.data.entity.*;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "vehicle-parts")
public class VehiclePartView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public VehiclePartView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehiclePart.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }

    private void processEvent(Optional<String> subcategory, Optional<Integer> urlParameter) {
        if (this.isbGrid()) {
            onAddEvent(ev -> {
                ZJTEntity entity = null;
                if (subcategory.isPresent()) {
                    ZJTVehicleServiceType serviceType = (ZJTVehicleServiceType) repository.findEntityById(ZJTVehicleServiceType.class, urlParameter.orElse(-1));
                    switch (subcategory.get()) {
                        case "service-type-kit":
                            entity = new ZJTServiceTypeKit();
                            ((ZJTServiceTypeKit) entity).setServiceType(serviceType);
                            break;
                        case "service-type-task":
                            entity = new ZJTServiceTypeTask();
                            ((ZJTServiceTypeTask) entity).setServiceType(serviceType);
                            break;
                        default:
                            break;
                    }
                } else
                    entity = new ZJTVehicleServiceType();

//                form.onNewItem((GuiItem) ev.getItem());
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
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        processEvent(event.getRouteParameters().get("subcategory"), event.getRouteParameters().getInteger("___url_parameter"));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            if (event.getRouteParameters().get("subcategory").isPresent()) {

            }
        }
    }
}
