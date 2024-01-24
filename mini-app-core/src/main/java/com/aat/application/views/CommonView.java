package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.componentfactory.tuigrid.event.ItemAddEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemChangeEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemDeleteEvent;
import com.vaadin.componentfactory.tuigrid.model.Cell;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class CommonView<T extends ZJTEntity> extends VerticalLayout implements RouterLayout, BeforeEnterObserver, HasDynamicTitle {

    protected final BaseEntityRepository<T> repository;
    protected Class<?> LayoutClass;
    protected int filterObjectId = -1;
    private String title = "";

    public CommonView(BaseEntityRepository<T> repository) {
        this.repository = repository;
    }

    protected void setForm(VerticalLayout form) {
        try {
            CoreMainLayout layout = (CoreMainLayout) LayoutClass.getDeclaredConstructor().newInstance();
            layout.setContent(form);
            Div outlet = new Div();
            outlet.setId("outlet");
            outlet.add(layout);
            Div selection = new Div();
            selection.setId("selection");
            UI.getCurrent().removeAll();
            UI.getCurrent().add(outlet);
            UI.getCurrent().add(selection);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String layoutClassName = (String) VaadinSession.getCurrent().getAttribute("layout");
        VaadinSession.getCurrent().setAttribute("previousView", UI.getCurrent().getInternals().getActiveViewLocation().getPath());

        if (layoutClassName != null) {
            try {
                LayoutClass = Class.forName(layoutClassName);
            } catch (ClassNotFoundException e) {
                event.rerouteToError(NotFoundException.class);
            }
        }

        if (event.getRouteParameters().getParameterNames().size() > 1) {
            filterObjectId = Integer.parseInt(event.getRouteParameters().get("___url_parameter").get());
        } else
            filterObjectId = -1;
    }

    @Override
    public String getPageTitle() {
        return title;
    }
}