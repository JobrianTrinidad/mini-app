package com.example.application.views;

import com.jo.application.core.form.CommonForm;
import com.jo.application.core.form.GridViewParameter;
import com.example.application.data.entity.ZJTVehicleKMReading;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.views.StandardFormView;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.*;

@Route(value = "vehiclekmreading/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleKMReadingView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;
    public VehicleKMReadingView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTVehicleKMReading.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        processEvent();
    }

    private void processEvent() {
        if (this.form != null && this.isbGrid()) {
            CommonForm form = this.form;
            onAddEvent(ev -> {
                form.onNewItem((GuiItem) ev.getItem());
                this.setMessageStatus("This is new added value " + ((GuiItem) ev.getItem()).getRecordData().get(1));
            });

            onUpdateEvent(ev -> {
                int count;
                try {
                    gridViewParameter.getHeaderTypeOptions().get(ev.getColName());
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

        this.setMessageStatus("This is a KM reading message status");

        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.INBOX));
        button.setTooltipText("Import Tele");

        button.addClickListener(e -> importTele());

        this.addCustomButton(button);
    }

    private void importTele() {
        this.setMessageStatus("Import tele successful!!!");
    }

}
