package com.aat.application.form;

import com.aat.application.core.form.StandardForm;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;

public class GridCommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService<T>> {
	public GridCommonForm(Class<T> entityClass, ZJTService<T> service) {
		super(entityClass, service);
		addClassName("demo-app-form");
	}
}