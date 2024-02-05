package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.CommonForm;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTServiceKit;
import com.aat.application.data.entity.ZJTUser;
import com.aat.application.data.entity.ZJTVehicleServiceSchedule;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
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

@Route(value = "user/:subcategory?/:filter?", layout = MainLayout.class)
public class UserView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;

    public UserView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTUser.class, "");
        gridViewParameter.setSelectDefinition("name");
        super.setGridViewParameter(gridViewParameter);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        processEvent();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
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

}
