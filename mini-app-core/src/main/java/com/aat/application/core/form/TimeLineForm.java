package com.aat.application.core.form;

import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TimeLineForm<S extends ZJTService> extends CommonForm {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    Timeline timeline;
    private final TimeLineViewParameter timeLineViewParameter;
    protected S service;

    public TimeLineForm(TimeLineViewParameter timeLineViewParameter,
                        S service) {
        this.timeLineViewParameter = timeLineViewParameter;
        this.service = service;
        addClassName("demo-app-form");

        configureTimeLine();
    }

    private VerticalLayout getToolbar() throws Exception {
        Button btnGoOriginView = new Button(GlobalData.convertToStandard(this.timeLineViewParameter.groupName));

//        VerticalLayout itemKindLayout = new VerticalLayout();
//        for (String fieldName : GlobalData.getFieldNamesWithAnnotation(StartDate.class, this.groupClass)) {
//            HorizontalLayout everyItemLayout = new HorizontalLayout();
//            everyItemLayout.setWidth(200, Unit.PIXELS);
//            everyItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//            String itemClassName = null;
//            for (Field field : this.groupClass.getDeclaredFields()) {
//                if (field.getName().equals(fieldName)) {
//                    itemClassName = field.getAnnotation(StartDate.class).className();
//                }
//            }
//            Span label = new Span(GlobalData.convertToStandard(fieldName));
//            Div graph = new Div();
//            graph.setWidth(50, Unit.PIXELS);
//            graph.setClassName(itemClassName);
//            everyItemLayout.add(label, graph);
//            itemKindLayout.add(everyItemLayout);
//        }

        String filteredValue = "";
        if (this.timeLineViewParameter.getParameters() != null) {
            if (!timeLineViewParameter.isValid()) {
                throw new Exception("TuiGrid Definition is not valid.");
            }
            if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getSelectDefinition() == null) {
                throw new Exception("Parameters are required, but not set");
            }

            StringBuilder query = new StringBuilder("SELECT p.").append(timeLineViewParameter.getSelectDefinition());

            query.append(" FROM ").append(timeLineViewParameter.getGroupClass().getSimpleName()).append(" as p");

            if (timeLineViewParameter.getWhereDefinition() != null && (int) timeLineViewParameter.getParameters()[0] != -1) {
                String[] whereDefinition = timeLineViewParameter.getWhereDefinition().split("\\.");
                query.append(" WHERE ").append("p.").append(whereDefinition[1]);
                //TODO -set parameter
                query.append(" = ").append(timeLineViewParameter.getParameters()[0]);
            }

            filteredValue = String.valueOf(service.findEntityByQuery(query.toString()).get(0));
        }
        Span sp = new Span(">> " + filteredValue);
        HorizontalLayout routeLayout = new HorizontalLayout(btnGoOriginView, sp);
        routeLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        btnGoOriginView.addClickListener(e -> {
            String previousView = (String) VaadinSession.getCurrent().getAttribute("previousView");
            if (previousView != null) {
                UI.getCurrent().navigate(previousView);
            }
        });
        btnGoOriginView.getElement().setAttribute("theme", "tertiary-inline");
        btnGoOriginView.addClassName("link-button");
        return new VerticalLayout(routeLayout);
    }

    private List<Object[]> configureGroup() throws Exception {
        if (!timeLineViewParameter.isValid()) {
            throw new Exception("TuiGrid Definition is not valid.");
        }
        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
            throw new Exception("Parameters are required, but not set");
        }

        String[] whereDefinition = timeLineViewParameter.getWhereDefinition().split("\\.");

        StringBuilder query = new StringBuilder("SELECT p.")
                .append(whereDefinition[1])
                .append(", p.").append(timeLineViewParameter.getSelectDefinition());


        query.append(" FROM ").append(timeLineViewParameter.getGroupClass().getSimpleName()).append(" as p");

        if ((int) timeLineViewParameter.getParameters()[0] != -1) {
            query.append(" WHERE ").append("p.").append(whereDefinition[1]);
            //TODO -set parameter
            query.append(" = ").append(timeLineViewParameter.getParameters()[0]);
        }
        return service.findEntityByQuery(query.toString());
    }

    private void configureTimeLine() {

        List<Item> items;
        try {
            items = getItems(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<ItemGroup> itemGroups;
        try {
            itemGroups = getGroupItems(configureGroup());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            zjtItem.setGroupId(e.getItem().getGroup());
            zjtItem.setClassName(e.getItem().getClassName());

//            service.save((T) zjtItem);
        });

        timeline.addItemUpdateTitle(e -> {
            Item item = e.getItem();

        });

        try {
            add(getToolbar(), timeline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve Items from DB to display
     *
     * @param flushRecords - if true, flush existing items in timeline, otherwise just add it
     * @return List<Item>
     */
    public List<Item> getItems(boolean flushRecords) throws Exception {
        List<Item> TableData = new ArrayList<>();
        if (!timeLineViewParameter.isValid()) {
            throw new Exception("Timeline Definition is not valid.");
        }
        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
            throw new Exception("Parameters are required, but not set");
        }

        String query = getStandardQuery(timeLineViewParameter.getParameters());

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

    private String getStandardQuery(Object[] parameters) {
        String query = "SELECT "
                + "p." + timeLineViewParameter.getTitleFieldName()
                + ", p." + timeLineViewParameter.getGroupIDFieldName()
                + ", p." + timeLineViewParameter.getStartDateFieldName()
                + (timeLineViewParameter.getEndDateFieldName() != null ? ", p." + timeLineViewParameter.getEndDateFieldName() : "")
                + (timeLineViewParameter.getClassNameFieldName() != null ? ", p." + timeLineViewParameter.getClassNameFieldName() : "");


        query = query +
                " FROM " + timeLineViewParameter.getFromDefinition() + " as p";

        if (timeLineViewParameter.getWhereDefinition() != null) {
            query = query +
                    " WHERE " + "p." + timeLineViewParameter.getWhereDefinition();
            //TODO -set parameter
            query = query + " = " + parameters[0];
        }
        return query;
    }

    private List<ItemGroup> getGroupItems(List<Object[]> groupResults) {
        if (groupResults == null)
            return null;
        List<ItemGroup> itemGroups = new ArrayList<>();
        for (Object[] groupResult :
                groupResults) {
            ItemGroup itemGroup = new ItemGroup();
            itemGroup.setTreeLevel(0);
            itemGroup.setNestedGroups(null);
            itemGroup.setClassName("");
            itemGroup.setVisible(true);
            itemGroup.setId((Integer) groupResult[0]);
            itemGroup.setContent((String) groupResult[1]);

            itemGroups.add(itemGroup);
        }
        return itemGroups;
    }

    @Override
    public String getHamburgerText() {
        return ">> ";
    }

    @Override
    public String getOriginViewText() {
//        return GlobalData.convertToStandard(this.gridViewParameter.groupName);
        return "";
    }
}