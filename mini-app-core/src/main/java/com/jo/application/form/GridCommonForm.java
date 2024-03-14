package com.jo.application.form;

import com.jo.application.core.form.GridViewParameter;
import com.jo.application.core.form.StandardForm;
import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.data.service.ZJTService;
import com.jo.application.data.service.BaseEntityService;
import com.jo.application.data.service.TableInfoService;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService> {
    public GridCommonForm(GridViewParameter gridViewParameter, BaseEntityService<T> service, TableInfoService tableInfoService) {
        super(gridViewParameter, service, tableInfoService);
        addClassName("demo-app-form");
    }
}