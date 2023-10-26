package com.aat.application.core.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.model.GroupItem;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

public abstract class TimeLineForm<T extends ZJTEntity, S extends ZJTService<T>> extends VerticalLayout {

    private static final long serialVersionUID = -5183438338263448740L;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    private Item newItem;
    private Button addItemButton;
    private String groupName = "group";
    private Class<? extends ZJTEntity> groupClass = null;
    Timeline timeline;
    protected S service;
    List<String> headers;
    Dictionary<String, String> headerOptions = new Hashtable<>();
    Dictionary<String, Class<?>> headerTypeOptions = new Hashtable<>();

    List<T> itemData = new ArrayList<>();

    public TimeLineForm(Class<T> entityClass, S service, String groupName, Class<? extends ZJTEntity> groupClass) {
        addClassName("demo-app-form");
        this.groupName = groupName;
        this.groupClass = groupClass;
        this.service = service;

        save = new Button("Save");
        close = new Button("Cancel");
        headers = configureHeader(entityClass);
        configureTimeLine();
    }

    private List<String> configureHeader(Class<T> entityClass) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> currentClass = entityClass;
        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(jakarta.persistence.Column.class) != null) {
                    fieldNames.add(field.getName());
                    headerOptions.put(field.getName(), "input");
                }
                if (field.getAnnotation(jakarta.persistence.Enumerated.class) != null) {
                    fieldNames.add(field.getName());
                    headerTypeOptions.put(field.getName(), field.getType());
                    headerOptions.put(field.getName(), "select_enum");
                }
                if (field.getAnnotation(jakarta.persistence.JoinColumn.class) != null) {
                    fieldNames.add(field.getName());
                    headerOptions.put(field.getName(), "select_class");
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fieldNames;
    }

    private void configureTimeLine() {
        List<Item> items = getItems();
        List<GroupItem> groupItems = getGroupItems();
        timeline = new Timeline(items, groupItems);

        // setting timeline range
        timeline.setTimelineRange(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2023, 12, 25, 0, 0, 0));

        timeline.setMultiselect(true);
        timeline.setWidthFull();
        boolean bAutoZoom = false;

        // Select Item
        TextField tfSelected = new TextField();

        VerticalLayout selectRangeLayout = getSelectRangeLayout(timeline, bAutoZoom, groupItems);
        HorizontalLayout zoomOptionsLayout = getSelectItemAndZoomOptionLayout(timeline, items, tfSelected, bAutoZoom);

//        add(selectRangeLayout, zoomOptionsLayout, timeline);
        add(timeline);
    }

    private List<Item> getItems() {

        List<Item> TableData = new ArrayList<>();
        if (filterText != null) itemData = service.findAll(filterText.getValue());
        else itemData = service.findAll(null);


        for (T data :
                itemData) {
            Item item = new Item();

            for (String header : headers) {
                try {
                    Field headerField = null;
                    Class<?> currentDataClass = data.getClass();
                    while (currentDataClass != null) {
                        try {
                            headerField = currentDataClass.getDeclaredField(header);
                            if (headerField != null) {
                                break;
                            }
                        } catch (NoSuchFieldException e) {
                            currentDataClass = currentDataClass.getSuperclass();
                        }
                    }
                    if (headerField == null) {
                        throw new RuntimeException("groupId field not found in class hierarchy");
                    }
                    headerField.setAccessible(true);
                    Object dataSel = headerField.get(data);
                    Field itemHeaderField = null;
                    try {
                        itemHeaderField = item.getClass().getDeclaredField(header);
                    } catch (NoSuchFieldException e) {
                        if (header.equals(this.groupName))
                            itemHeaderField = item.getClass().getDeclaredField("group");
                        else
                            continue;
                    }
                    itemHeaderField.setAccessible(true);
                    switch (headerOptions.get(header)) {
                        case "input":
                            itemHeaderField.set(item, dataSel);
                            break;
                        case "select_class":
                            if (dataSel == null)
                                dataSel = data.getClass().getDeclaredField(header);
                            Field itemIdField = null;
                            Class<?> currentDataSelClass = dataSel.getClass();
                            while (currentDataSelClass != null) {
                                try {
                                    itemIdField = currentDataSelClass.getDeclaredField("groupId");
                                    if (itemIdField != null) {
                                        break;
                                    }
                                } catch (NoSuchFieldException e) {
                                    currentDataSelClass = currentDataSelClass.getSuperclass();
                                }
                            }
                            if (itemIdField == null) {
                                throw new RuntimeException("groupId field not found in class hierarchy");
                            }
                            itemIdField.setAccessible(true);
                            itemHeaderField.set(item, String.valueOf(itemIdField.get(dataSel)));

                            String headerName = header.substring(0, 1).toUpperCase()
                                    + header.substring(1);
//                            GlobalData.addData(headerName);
                            break;
                        default:
                            break;
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            TableData.add(item);

        }
        return TableData;
    }

    private List<GroupItem> getGroupItems() {
        List<GroupItem> groupItems = new ArrayList<>();
        GlobalData.addData(groupName, groupClass);
        List<ZJTEntity> groupResults = GlobalData.listData.get(groupName);
        for (Object groupResult :
                groupResults) {
            GroupItem groupItem = new GroupItem();
            for (Field field :
                    groupItem.getClass().getDeclaredFields()) {
                try {
                    Field headerField = null;
                    Class<?> groupResultClass = groupResult.getClass();
                    while (groupResultClass != null) {
                        try {
                            headerField = groupResultClass.getDeclaredField(field.getName());
                            if (headerField != null) {
                                break;
                            }
                        } catch (NoSuchFieldException e) {
                            groupResultClass = groupResultClass.getSuperclass();
                        }
                    }
                    if (headerField == null) {
                        throw new RuntimeException("groupId field not found in class hierarchy");
                    }
                    headerField.setAccessible(true);
                    field.setAccessible(true);
                    field.set(groupItem, headerField.get(groupResult));
                } catch (IllegalAccessException ignored) {
                }
            }
            groupItems.add(groupItem);
        }
        return groupItems;
    }

    private VerticalLayout getSelectRangeLayout(Timeline timeline, boolean bAutoZoom, List<GroupItem> groupItems) {
        VerticalLayout selectRangeLayout = new VerticalLayout();
        selectRangeLayout.setSpacing(false);
        Paragraph p = new Paragraph("Select range for new item: ");
        p.getElement().getStyle().set("margin-bottom", "5px");
        selectRangeLayout.add(p);

        ComboBox<GroupItem> comboBox = new ComboBox<>("Group Name");
        comboBox.setItems(groupItems);
        comboBox.setItemLabelGenerator(GroupItem::getContent);
        comboBox.setRenderer(createRenderer());
        comboBox.setValue(groupItems.get(0));
        comboBox.setAllowCustomValue(true);

        DateTimePicker datePicker1 = new DateTimePicker("Item start date: ");
        datePicker1.setMin(LocalDateTime.of(2023, 1, 10, 0, 0, 0));
        datePicker1.setMax(LocalDateTime.of(2023, 8, 22, 0, 0, 0));

        DateTimePicker datePicker2 = new DateTimePicker("Item end date: ");
        datePicker2.setMin(LocalDateTime.of(2023, 1, 10, 0, 0, 0));
        datePicker2.setMax(LocalDateTime.of(2023, 8, 22, 0, 0, 0));

        datePicker1.addValueChangeListener(e -> {
            GroupItem selectedGroupItem = comboBox.getValue();
            newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
        });
        datePicker2.addValueChangeListener(e -> {
            GroupItem selectedGroupItem = comboBox.getValue();
            newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
        });

        comboBox.addValueChangeListener(e -> {
            GroupItem selectedGroupItem = comboBox.getValue();
            newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(datePicker1, datePicker2, comboBox);

        addItemButton = new Button("Add Item", e -> {
            timeline.addItem(newItem, bAutoZoom);
            newItem = null;
            datePicker1.clear();
            datePicker2.clear();
        });
        addItemButton.setDisableOnClick(true);
        addItemButton.setEnabled(false);

        selectRangeLayout.add(horizontalLayout, addItemButton);
        return selectRangeLayout;
    }

    private HorizontalLayout getSelectItemAndZoomOptionLayout(Timeline timeline, List<Item> items, TextField textField, boolean bAutoZoom) {
        VerticalLayout selectLayout = new VerticalLayout();
        Button setSelectBtn = new Button("Select Item", e -> timeline.setSelectItem(textField.getValue()));
        selectLayout.add(textField, setSelectBtn);

        HorizontalLayout zoomOptionsLayout = new HorizontalLayout();
        zoomOptionsLayout.setMargin(true);
        Button oneDay = new Button("1 day", e -> timeline.setZoomOption(1));
        Button threeDays = new Button("3 days", e -> timeline.setZoomOption(3));
        Button fiveDays = new Button("5 days", e -> timeline.setZoomOption(5));

        zoomOptionsLayout.add(oneDay, threeDays, fiveDays, selectLayout);

        timeline.addItemSelectListener(e -> {
            timeline.onSelectItem(e.getTimeline(), e.getItemId(), bAutoZoom);
            textField.setValue(e.getItemId());
        });

        timeline.addGroupItemClickListener(e -> {
            StringBuilder temp = new StringBuilder();
            for (Item item : items) {
                if (Integer.parseInt(item.getGroup()) == Integer.parseInt(e.getGroupId())) {
                    if (temp.length() > 0) temp.append(",").append(item.getId());
                    else temp.append(item.getId());

                }
            }
            e.getTimeline().onSelectItem(e.getTimeline(), temp.toString(), false);
        });


        return zoomOptionsLayout;
    }

    private Item createNewItem(LocalDateTime start, LocalDateTime end, int groupID) {
        if (start != null && end != null) {
            if (start.isBefore(end)) {
                addItemButton.setEnabled(true);
                return new Item(start, end, groupID);
            } else {
                Notification.show("End date should be after start date", 5000, Notification.Position.MIDDLE);
                return null;
            }
        } else {
            addItemButton.setEnabled(false);
            return null;
        }
    }

    private Renderer<GroupItem> createRenderer() {

        return LitRenderer.<GroupItem>of("<span style= \"font-weight: ${item.width}; font-size: ${item.fontsize}\">${item.content}</span>").withProperty("width", groupItem -> {
            if (groupItem.getTreeLevel() == 1) return "bolder";
            else if (groupItem.getTreeLevel() == 2) return "bold";
            else return "normal";
        }).withProperty("fontsize", groupItem -> {
            if (groupItem.getTreeLevel() == 1) return "1rem";
            else if (groupItem.getTreeLevel() == 2) return "0.9rem";
            else return "0.8rem";
        }).withProperty("content", GroupItem::getContent);
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterText);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, close);
    }
}