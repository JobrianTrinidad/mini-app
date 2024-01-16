package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.TimeLineViewParameter;
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

@Route(value = "user/:subcategory?/:filter?", layout = MainLayout.class)
public class UserView extends StandardFormView<ZJTEntity> implements HasUrlParameter<String> {

    public UserView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }


}
