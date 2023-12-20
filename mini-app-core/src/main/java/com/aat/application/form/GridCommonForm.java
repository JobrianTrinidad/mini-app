package com.aat.application.form;

import com.aat.application.core.form.StandardForm;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.service.TableInfoService;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService<T>> {
	public GridCommonForm(Class<T> entityClass, ZJTService<T> service, TableInfoService tableInfoService, String groupName, int filterObjectId) {
		super(entityClass, service, tableInfoService, groupName, filterObjectId);
		addClassName("demo-app-form");
	}
}