package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.TimeLineCommonForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Route(value = "timeline")
public class TimeLineFormView<T extends ZJTEntity> extends CommonView<T> {

    protected TimeLineCommonForm<T> form;

    public TimeLineFormView(BaseEntityRepository<T> repository) {
        super(repository);
    }

    private void configureForm() {
        form = new TimeLineCommonForm<>(entityClass, new BaseEntityService<>(repository));
        setForm(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);

        if (entityClass != null)
            configureForm();
    }
}