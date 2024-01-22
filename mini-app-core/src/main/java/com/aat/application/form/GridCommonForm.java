package com.aat.application.form;

import com.aat.application.core.form.StandardForm;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.componentfactory.tuigrid.event.ItemAddEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemChangeEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemDeleteEvent;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;

import java.util.function.Consumer;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService<T>> {
    public GridCommonForm(Class<T> entityClass, Class<T> filteredEntityClass, ZJTService<T> service, TableInfoService tableInfoService, String groupName, int filterObjectId) {
        super(entityClass, filteredEntityClass, service, tableInfoService, groupName, filterObjectId);
        addClassName("demo-app-form");
    }
}