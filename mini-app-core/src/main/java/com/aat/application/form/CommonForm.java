package com.aat.application.form;

import com.aat.application.core.StandardForm;
import com.aat.application.core.ZJTEntity;
import com.aat.application.core.ZJTService;

public class CommonForm<T extends ZJTEntity> extends StandardForm<T, ZJTService<T>> {
	public CommonForm(Class<T> entityClass, ZJTService<T> service) {
		super(entityClass, service);
		addClassName("demo-app-form");
	}
}