package com.aat.application.core.form;

import com.aat.application.annotations.timeline.*;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import jakarta.persistence.Id;

import java.io.Serial;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TimeLineForm<T extends ZJTEntity, S extends ZJTService<T>> extends CommonForm<T> {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    private final String groupName;
    Timeline timeline;
    TimeLineViewParameter timeLineViewParameter;
    protected S service;
    private final Class<T> groupClass;
    private Class<T> filteredEntityClass;

    public TimeLineForm(Class<T> groupClass,
                        TimeLineViewParameter timeLineViewParameter,
                        Class<T> filteredEntityClass,
                        S service, String groupName, int filterObjectId) {
        this.groupClass = groupClass;
        this.timeLineViewParameter = timeLineViewParameter;
        this.filteredEntityClass = filteredEntityClass;
        this.service = service;
        addClassName("demo-app-form");
        this.groupName = groupName;

        configureGroup();
        configureTimeLine(filterObjectId);
    }

    private VerticalLayout getToolbar(int filterObjectId) {
        Button btnGoOriginView = new Button(GlobalData.convertToStandard(this.groupName));

        VerticalLayout itemKindLayout = new VerticalLayout();
        for (String fieldName : GlobalData.getFieldNamesWithAnnotation(StartDate.class, this.groupClass)) {
            HorizontalLayout everyItemLayout = new HorizontalLayout();
            everyItemLayout.setWidth(200, Unit.PIXELS);
            everyItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            String itemClassName = null;
            for (Field field : this.groupClass.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    itemClassName = field.getAnnotation(StartDate.class).className();
                }
            }
            Span label = new Span(GlobalData.convertToStandard(fieldName));
            Div graph = new Div();
            graph.setWidth(50, Unit.PIXELS);
            graph.setClassName(itemClassName);
            everyItemLayout.add(label, graph);
            itemKindLayout.add(everyItemLayout);
        }

        String filteredValue = "";
        if (filterObjectId != -1)
            for (Object data :
                    GlobalData.listData.get(this.groupName)) {
                Field pkFiled = GlobalData.getPrimaryKeyField(data.getClass());
                pkFiled.setAccessible(true);
                try {
                    if ((int) pkFiled.get(data) == filterObjectId)
                        filteredValue = GlobalData.getContentDisplayedInSelect(data);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        Span sp = new Span(">> " + filteredValue);
        HorizontalLayout routeLayout = new HorizontalLayout(btnGoOriginView, sp);
        routeLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        btnGoOriginView.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", this.filteredEntityClass.getName());
            String previousView = (String) VaadinSession.getCurrent().getAttribute("previousView");
            if (previousView != null) {
                UI.getCurrent().navigate(previousView);
            }
        });
        btnGoOriginView.getElement().setAttribute("theme", "tertiary-inline");
        btnGoOriginView.addClassName("link-button");
        return new VerticalLayout(itemKindLayout, routeLayout);
    }

    private void configureGroup() {
        Class<?> currentClass = this.groupClass;
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.getAnnotation(jakarta.persistence.JoinColumn.class) != null) {
                    GlobalData.addData(field.getName(), (Class<? extends ZJTEntity>) field.getType());
                    if (field.getName().equals(this.groupName))
                        this.filteredEntityClass = (Class<T>) field.getType();
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private void configureTimeLine(int filterObjectId) {

        String fieldName = "";
        for (Field field : groupClass.getDeclaredFields()) {
            if (field.getName().equals(groupName)) {
                for (Field childField : field.getType().getDeclaredFields()) {
                    if (childField.getAnnotation(Id.class) != null) {
                        fieldName = groupName + "." + childField.getName();
                    }
                }
            }
        }

        List<Item> items;
        try {
            items = getItems(timeLineViewParameter, new Integer[]{filterObjectId}, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<ItemGroup> itemGroups = getGroupItems((List<ZJTEntity>) GlobalData.listData.get(groupName), fieldName, filterObjectId);
        if (itemGroups == null)
            timeline = new Timeline(items);
        else
            timeline = new Timeline(items, itemGroups);

        timeline.setTimelineRange(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0, 0));

        timeline.setMultiselect(true);
        timeline.setStack(true);
        timeline.setWidthFull();
        timeline.addItemAddListener(e -> {
            ZJTItem zjtItem = new ZJTItem();
            zjtItem.setTitle(e.getItem().getTitle());
            zjtItem.setContent(e.getItem().getContent());
            zjtItem.setStartTime(e.getItem().getStart());
            zjtItem.setEndTime(e.getItem().getEnd());
            zjtItem.setGroupId(this.groupName + "-" + e.getItem().getGroup());
            zjtItem.setClassName(e.getItem().getClassName());

            service.save((T) zjtItem);
        });

        timeline.addItemUpdateTitle(e -> {
            Item item = e.getItem();

        });

        add(getToolbar(filterObjectId), timeline);
    }

    /**
     * Retrieve Items from DB to display
     *
     * @param td           - Timeline view definition
     * @param parameters   - setParameters if applicable
     * @param flushRecords - if true, flush existing items in timelime, otherwise just add it
     * @return
     */
    public List<Item> getItems(TimeLineViewParameter td, Object[] parameters, boolean flushRecords) throws Exception {
        List<Item> TableData = new ArrayList<>();
        if (!td.isValid()) {
            throw new Exception("Timeline Definition is not valid.");
        }
        if (td.isRequireParameter() && parameters == null) {
            throw new Exception("Parameters are required, but not set");
        }

        String query = "SELECT "
                + "p." + td.getTitleFieldName()
                + ", p." + td.getGroupIDFieldName()
                + ", p." + td.getStartDateFieldName()
                + (td.getEndDateFieldName() != null ? ", p." + td.getEndDateFieldName() : "")
                + (td.getClassNameFieldName() != null ? ", p." + td.getClassNameFieldName() : "");


        query = query +
                " FROM " + td.getFromDefinition() + " as p";

        if (td.getWhereDefinition() != null) {
            query = query +
                    " WHERE " + "p." + td.getWhereDefinition();
            //TODO -set parameter
            query = query + " = " + parameters[0];
        }

        for (ZJTItem data :
                service.findByQuery(query)) {
            Item item = new Item();
//            item.setId(data.getId().toString());
            item.setContent(data.getTitle());
            item.setClassName(data.getClassName());
            item.setStart(data.getStartTime());
            item.setEnd(data.getEndTime());
            item.setGroup(data.getGroupId());

            TableData.add(item);
        }
        return TableData;
    }

    private List<ItemGroup> getGroupItems(List<ZJTEntity> groupResults, String fieldName, int fieldId) {
        if (groupResults == null)
            return null;
        List<ItemGroup> itemGroups = new ArrayList<>();
        for (Object groupResult :
                groupResults) {
            ItemGroup itemGroup = new ItemGroup();
            itemGroup.setTreeLevel(0);
            itemGroup.setNestedGroups(null);
            itemGroup.setClassName("");
            itemGroup.setVisible(true);
            for (Field field : groupResult.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    if (field.getAnnotation(jakarta.persistence.Id.class) != null) {
                        itemGroup.setId((Integer) field.get(groupResult));
                        continue;
                    }
                    for (Annotation annotation : field.getAnnotations()) {
                        if (annotation.annotationType().getSimpleName().equals("ContentDisplayedInSelect")) {
                            itemGroup.setContent((String) field.get(groupResult));
                            break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fieldId == itemGroup.getGroupId())
                itemGroups.add(itemGroup);
        }
        return itemGroups;
    }
}