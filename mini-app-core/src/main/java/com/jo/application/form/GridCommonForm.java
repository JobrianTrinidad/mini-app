package com.jo.application.form;

import com.jo.application.core.form.GridViewParameter;
import com.jo.application.core.form.StandardForm;
import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.data.service.ZJTService;
import com.jo.application.data.service.BaseEntityService;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.flow.component.button.Button;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService> {
    public GridCommonForm(GridViewParameter gridViewParameter, BaseEntityService<T> service, TableInfoService tableInfoService) {
        super(gridViewParameter, service, tableInfoService);
        addClassName("demo-app-form");
    }

    @Override
    public void addCustomButton(int index, Button button) {
        super.addCustomButton(index, button);
    }

}