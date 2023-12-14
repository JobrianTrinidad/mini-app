package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.componentfactory.tuigrid.model.Cell;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class CommonView<T extends ZJTEntity> extends VerticalLayout implements RouterLayout, BeforeEnterObserver, HasDynamicTitle {

    protected final BaseEntityRepository<T> repository;
    protected Class<T> entityClass;
    protected Class<T> filteredEntityClass;
    protected Class<?> LayoutClass;
    protected Class<? extends ZJTEntity> groupClass;
    protected String groupName;
    protected ArrayList<Cell> filterTemp;
    protected boolean bFilter = false;
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
        String entityClassName = (String) VaadinSession.getCurrent().getAttribute("entityClass");
        String filteredEntityClassName = (String) VaadinSession.getCurrent().getAttribute("filteredEntityClass");
        String layoutClassName = (String) VaadinSession.getCurrent().getAttribute("layout");
        filterTemp = (ArrayList<Cell>) VaadinSession.getCurrent().getAttribute("filter");
        groupName = (String) VaadinSession.getCurrent().getAttribute("groupName");
        String groupClassName = (String) VaadinSession.getCurrent().getAttribute("groupClass");
        if(groupClassName != null){
            try {
                groupClass = (Class<? extends ZJTEntity>) Class.forName(groupClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (filteredEntityClassName != null && !filteredEntityClassName.isEmpty())
            try {
                bFilter = true;
                filteredEntityClass = (Class<T>) Class.forName(filteredEntityClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        else
            bFilter = false;

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

        // Remove session data
        VaadinSession.getCurrent().setAttribute("entityClass", null);
        VaadinSession.getCurrent().setAttribute("filteredEntityClass", null);
//        VaadinSession.getCurrent().setAttribute("layout", null);
        VaadinSession.getCurrent().setAttribute("filter", null);
        VaadinSession.getCurrent().setAttribute("groupName", null);
        VaadinSession.getCurrent().setAttribute("groupClass", null);
    }

    @Override
    public String getPageTitle() {
        return title;
    }
}