package com.jo.application.views;

import com.jo.application.core.form.CommonForm;
import com.jo.application.core.form.GridViewParameter;
import com.jo.application.data.entity.ZJTServiceTypeKit;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "service-type-kit/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceTypeKitView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public ServiceTypeKitView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTServiceTypeKit.class, "");
        super.setGridViewParameter(gridViewParameter);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
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
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
    }

}
