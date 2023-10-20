package com.aat.application.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.core.form.TimeLineForm;

public class TimeLineCommonForm<T extends ZJTEntity> extends TimeLineForm<T, ZJTService<T>> {
	public TimeLineCommonForm(Class<T> entityClass, ZJTService<T> service) {
		super(entityClass, service);
		addClassName("demo-app-form");
	}
}