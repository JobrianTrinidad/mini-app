package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.componentfactory.tuigrid.model.Cell;
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
    public Class<T> entityClass;
    protected Class<T> filteredEntityClass;
    protected Class<?> LayoutClass;
    protected Class groupClass;
    protected int filterObjectId = -1;
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
        String entityClassName = (String) VaadinSession.getCurrent().getAttribute("entityClass");
        String layoutClassName = (String) VaadinSession.getCurrent().getAttribute("layout");
        VaadinSession.getCurrent().setAttribute("previousView", UI.getCurrent().getInternals().getActiveViewLocation().getPath());
        if (event.getRouteParameters().getParameterNames().contains("subcategory")) {
            this.groupClass = ((CommonView) UI.getCurrent().getInternals().getActiveRouterTargetsChain().get(0)).entityClass;
            filteredEntityClass = this.groupClass;
        }

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

        if (this.groupClass != null) {
            assert this.entityClass != null;
            for (Field field : this.entityClass.getDeclaredFields()) {
                if (field.getType().getSimpleName().equals(this.groupClass.getSimpleName())) {
                    groupName = field.getName();
                }
            }
        }

        if (entityClass == null) {
            event.rerouteToError(NotFoundException.class);
        } else {
            if (repository != null)
                repository.setEntityClass(entityClass);
        }

        if (event.getRouteParameters().getParameterNames().size() > 1) {
            filterObjectId = Integer.parseInt(event.getRouteParameters().get("___url_parameter").get());
            for (Field field : this.entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType().getSimpleName().equals(filteredEntityClass.getSimpleName())) {
                    groupName = field.getName();
                    break;
                }
            }
        } else
            filterObjectId = -1;
//        String groupClassName = (String) VaadinSession.getCurrent().getAttribute("groupClass");
//        if (groupClassName != null) {
//            try {
//                groupClass = (Class<? extends ZJTEntity>) Class.forName(groupClassName);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }

//        if (filteredEntityClassName != null && !filteredEntityClassName.isEmpty())
//            try {
//                bFilter = true;
//                filteredEntityClass = (Class<T>) Class.forName(filteredEntityClassName);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        else
//            bFilter = false;


        // Remove session data
//        VaadinSession.getCurrent().setAttribute("entityClass", null);
//        VaadinSession.getCurrent().setAttribute("filteredEntityClass", null);
////        VaadinSession.getCurrent().setAttribute("layout", null);
//        VaadinSession.getCurrent().setAttribute("filter", null);
//        VaadinSession.getCurrent().setAttribute("groupName", null);
//        VaadinSession.getCurrent().setAttribute("groupClass", null);
    }

    @Override
    public String getPageTitle() {
        return title;
    }
}