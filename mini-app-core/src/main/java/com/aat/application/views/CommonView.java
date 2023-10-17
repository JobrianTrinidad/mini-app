package com.aat.application.views;

import com.aat.application.core.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.form.CommonForm;
import com.aat.application.util.GlobalData;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class CommonView<T extends ZJTEntity> extends VerticalLayout implements RouterLayout, BeforeEnterObserver, HasDynamicTitle {

    protected CommonForm<T> form;
    protected final BaseEntityRepository<T> repository;
    protected Class<T> entityClass;
    private String title = "";

    public CommonView(BaseEntityRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        queryParameters.getParameters().forEach((key, values) -> {
            values.forEach(value -> parameters.add(key, value));
        });
        String entityClassName = parameters.getFirst("entityClass");
        if (entityClassName != null) {
            try {
                entityClass = (Class<T>) Class.forName(GlobalData.ENTITY_PATH + "." + entityClassName);
                PageTitle pageTitleAnnotation = entityClass.getAnnotation(PageTitle.class);
                if (pageTitleAnnotation != null) {
                    title = pageTitleAnnotation.value();
                }
            } catch (ClassNotFoundException e) {
                event.rerouteToError(NotFoundException.class);
            }
        }

        if (entityClass == null) {
            event.rerouteToError(NotFoundException.class);
        } else {
            repository.setEntityClass(entityClass);
        }
    }

    @Override
    public String getPageTitle() {
        return title;
    }
}