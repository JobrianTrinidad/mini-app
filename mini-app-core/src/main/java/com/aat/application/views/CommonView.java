package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;

public abstract class CommonView<T extends ZJTEntity> extends VerticalLayout implements RouterLayout, BeforeEnterObserver, HasDynamicTitle {

    protected final BaseEntityRepository<T> repository;
    protected Class<T> entityClass;
    protected Class<?> LayoutClass;
    private String title = "";

    public CommonView(BaseEntityRepository<T> repository) {
        this.repository = repository;
    }

    protected void setForm(VerticalLayout form) {
        try {
            CoreMainLayout layout = (CoreMainLayout) LayoutClass.getDeclaredConstructor().newInstance();
            layout.setContent(form);
            UI.getCurrent().removeAll();
            UI.getCurrent().add(layout);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        queryParameters.getParameters().forEach((key, values) -> {
            values.forEach(value -> parameters.add(key, value));
        });
        String entityClassName = (String) VaadinSession.getCurrent().getAttribute("entityClass");
        String layoutClassName = (String) VaadinSession.getCurrent().getAttribute("layout");

        if (entityClassName != null && layoutClassName != null) {
            try {
                entityClass = (Class<T>) Class.forName(entityClassName);
                LayoutClass = Class.forName(layoutClassName);
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