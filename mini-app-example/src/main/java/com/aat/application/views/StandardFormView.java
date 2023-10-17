package com.aat.application.views;

import com.aat.application.core.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.CommonForm;
import com.vaadin.flow.router.*;

@Route(value = "commonview", layout = MainLayout.class)
public class StandardFormView<T extends ZJTEntity> extends CommonView<T> {

    protected CommonForm<T> form;
    protected final BaseEntityRepository<T> repository;

    public StandardFormView(BaseEntityRepository<T> repository) {
        super(repository);
        this.repository = repository;
    }

    private void configureForm() {
        if (form != null)
            remove(form);

        form = new CommonForm<>(entityClass, new BaseEntityService<>(repository));
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