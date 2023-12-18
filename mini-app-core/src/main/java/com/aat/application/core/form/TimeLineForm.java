package com.aat.application.core.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.data.service.TimelineService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;

import java.io.Serial;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TimeLineForm<T extends ZJTEntity> extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    private final String groupName;
    Timeline timeline;
    List<ZJTItem> itemData;
    private final TimelineService timelineService;
    private final Class<T> groupClass;
    private Class<T> filteredEntityClass;

    public TimeLineForm(Class<T> groupClass, TimelineService timelineService, String groupName) {
        this.groupClass = groupClass;
        this.timelineService = timelineService;
        addClassName("demo-app-form");
        this.groupName = groupName;

        save = new Button("Save");
        close = new Button("Cancel");
        configureGroup();
        configureTimeLine();
    }

    private HorizontalLayout getToolbar() {
//        Span sp = new Span(">> " + filteredValue);
        Button btnGoOriginView = new Button(this.groupName);
        HorizontalLayout routeLayout = new HorizontalLayout(btnGoOriginView);
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
        return routeLayout;
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

    private void configureTimeLine() {
        List<Item> items = getItems();
        List<ItemGroup> itemGroups = getGroupItems((List<ZJTEntity>) GlobalData.listData.get(groupName));
        if (itemGroups == null)
            timeline = new Timeline(items);
        else
            timeline = new Timeline(items, itemGroups);

        timeline.setTimelineRange(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2023, 12, 25, 0, 0, 0));

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

            timelineService.save(zjtItem);
        });

        timeline.addItemUpdateTitle(e -> {
            Item item = e.getItem();

        });

        add(getToolbar(), timeline);
    }

    private List<Item> getItems() {

        List<Item> TableData = new ArrayList<>();
        if (filterText != null) itemData = timelineService.findAll();
        else itemData = timelineService.findAllByFilter(null);

        for (ZJTItem data :
                itemData) {
            Item item = new Item();
            item.setId(data.getId().toString());
            item.setContent(data.getTitle());
            item.setClassName(data.getClassName());
            item.setStart(data.getStartTime());
            item.setEnd(data.getEndTime());
            String[] tempGroupId = data.getGroupId().split("-");
            item.setGroup(tempGroupId[1]);

            TableData.add(item);
        }
        return TableData;
    }

    private List<ItemGroup> getGroupItems(List<ZJTEntity> groupResults) {
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

            itemGroups.add(itemGroup);
        }
        return itemGroups;
    }
}