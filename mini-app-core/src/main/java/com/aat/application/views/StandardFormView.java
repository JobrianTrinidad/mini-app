package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.GridCommonForm;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.*;

@Route(value = "commonview", layout = CoreMainLayout.class)
public class StandardFormView<T extends ZJTEntity> extends CommonView<T> {

    protected GridCommonForm<T> form;

    public StandardFormView(BaseEntityRepository<T> repository) {
        super(repository);
    }

    private void configureForm() {
        if (form != null)
            remove(form);

        form = new GridCommonForm<>(entityClass, new BaseEntityService<>(repository));
        form.setWidth("25em");
        add(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        if (entityClass != null)
            configureForm();
    }

}