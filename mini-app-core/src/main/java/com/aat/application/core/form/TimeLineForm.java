package com.aat.application.core.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
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
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class TimeLineForm<T extends ZJTEntity, S extends ZJTService<T>> extends VerticalLayout {

    private static final long serialVersionUID = -5183438338263448740L;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    private Item newItem;
    private Button addItemButton;
    Timeline timeline;
    protected S service;

    List<T> itemData = new ArrayList<>();

    public TimeLineForm(Class<T> entityClass, S service) {
        addClassName("demo-app-form");
        this.service = service;

//        binder = new BeanValidationBinder<>(entityClass);
        save = new Button("Save");
        close = new Button("Cancel");

        configureTimeLine();
    }

    private void configureTimeLine() {
        List<Item> items = getItems();
        List<GroupItem> groupItems = getGroupItems();
        timeline = new Timeline(items, groupItems);

        // setting timeline range
        timeline.setTimelineRange(
                LocalDateTime.of(2023, 1, 1, 0, 0, 0),
                LocalDateTime.of(2023, 9, 25, 0, 0, 0));

        timeline.setMultiselect(true);
        timeline.setWidthFull();
        boolean bAutoZoom = false;

        // Select Item
        TextField tfSelected = new TextField();

        VerticalLayout selectRangeLayout = getSelectRangeLayout(timeline, bAutoZoom, groupItems);
        HorizontalLayout zoomOptionsLayout = getSelectItemAndZoomOptionLayout(timeline, items, tfSelected, bAutoZoom);

        add(selectRangeLayout, zoomOptionsLayout, timeline);
    }

    private List<Item> getItems() {

        List<Item> TableData = new ArrayList<>();
//        if (filterText != null)
//            itemData = service.findAll(filterText.getValue());
//        else
            itemData = service.findAll(null);

        Comparator<T> comparator = Comparator.comparing(ZJTEntity::getName);
        itemData.sort(comparator);

        for (T data :
                itemData) {
            try {
                Item item = getItem(data);
                item.setEditable(true);
                item.setUpdateTime(true);
                TableData.add(item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

//        Item item1 = new Item(
//                LocalDateTime.of(2023, 8, 11, 2, 30, 0),
//                LocalDateTime.of(2023, 8, 11, 7, 0, 0),
//                "Item 1", 1);
//        item1.setId("0");
//        item1.setEditable(true);
//        item1.setUpdateTime(true);
//
//        Item item2 = new Item(
//                LocalDateTime.of(2023, 8, 13, 0, 0, 0),
//                LocalDateTime.of(2023, 8, 13, 12, 0, 0),
//                "Item 2", 6);
//        item2.setId("1");
//        item2.setEditable(true);
//        item2.setUpdateTime(true);
//
//        Item item3 = new Item(
//                LocalDateTime.of(2023, 8, 14, 2, 30, 0),
//                LocalDateTime.of(2023, 8, 15, 1, 0, 0),
//                "Item 3", 100);
//        item3.setId("2");
//        item3.setEditable(true);
//        item3.setUpdateTime(true);
//
//        Item item4 = new Item(
//                LocalDateTime.of(2023, 8, 16, 1, 30, 0),
//                LocalDateTime.of(2023, 8, 17, 1, 0, 0),
//                "Item 4", 106);
//        item4.setId("3");
//        item4.setEditable(true);
//        item4.setUpdateTime(true);
//
//        return Arrays.asList(item1, item2, item3, item4);
        return TableData;
    }

    private static <T extends ZJTEntity> Item getItem(T data) throws NoSuchFieldException, IllegalAccessException {
        Field startTimeField = data.getClass().getDeclaredField("startTime");
        startTimeField.setAccessible(true);
        Object startTimeVal = startTimeField.get(data);
        Field endTimeField = data.getClass().getDeclaredField("endTime");
        endTimeField.setAccessible(true);
        Object endTimeVal = endTimeField.get(data);
        Field nameField = data.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        Object nameVal = nameField.get(data);
        Field idField = data.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        Object idVal = idField.get(data);
        Item item = new Item(
                (LocalDateTime) startTimeVal,
                (LocalDateTime) endTimeVal,
                (String) nameVal, 1);
        item.setId(String.valueOf(idVal));
        return item;
    }

    private List<GroupItem> getGroupItems() {

        GroupItem groupItem1243 = new GroupItem(1243, "Level 3 1243", true, 2);
        GroupItem groupItem1525 = new GroupItem(1525, "Level 3 1525", true, 2);
        GroupItem groupItem1624 = new GroupItem(1624, "Level 3 1624", true, 2);
        GroupItem groupItem2076 = new GroupItem(2076, "Level 3 2076", true, 2);
        GroupItem groupItem1345 = new GroupItem(1345, "Level 3 1345", true, 2);
        GroupItem groupItem2078 = new GroupItem(2078, "Level 3 2078", true, 2);
        GroupItem groupItem1826 = new GroupItem(1826, "Level 3 1826", true, 2);
        GroupItem groupItem2107 = new GroupItem(2107, "Level 3 2107", true, 2);
        GroupItem groupItem10 = new GroupItem(10, "Group 10", "1,2,3,4,5,6", true, 0);
        GroupItem groupItem1 = new GroupItem(1, "North America", "1243,1525,1624,1345,2078,1826,2076,2107",
                true, 1);
        GroupItem groupItem2 = new GroupItem(2, "Latin America", true, 1);
        GroupItem groupItem3 = new GroupItem(3, "Europe", true, 1);
        GroupItem groupItem4 = new GroupItem(4, "Asia", true, 1);
        GroupItem groupItem5 = new GroupItem(5, "Oceania", true, 1);
        GroupItem groupItem6 = new GroupItem(6, "Africa", true, 1);
        GroupItem groupItem100 = new GroupItem(100, "Group 100", "101, 102, 103, 104, 105, 106", true, 0);
        GroupItem groupItem101 = new GroupItem(101, "North America", true, 1);
        GroupItem groupItem102 = new GroupItem(102, "Latin America", true, 1);
        GroupItem groupItem103 = new GroupItem(103, "Europe", true, 1);
        GroupItem groupItem104 = new GroupItem(104, "Asia", true, 1);
        GroupItem groupItem105 = new GroupItem(105, "Oceania", true, 1);
        GroupItem groupItem106 = new GroupItem(106, "Africa", true, 1);

        return Arrays.asList(groupItem10, groupItem1, groupItem1243, groupItem1525, groupItem1624, groupItem2076,
                groupItem1345, groupItem2078, groupItem1826, groupItem2107,
                groupItem2, groupItem3, groupItem4, groupItem5, groupItem6, groupItem100, groupItem101,
                groupItem102, groupItem103, groupItem104, groupItem105, groupItem106);
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

        datePicker1.addValueChangeListener(
                e -> {
                    GroupItem selectedGroupItem = comboBox.getValue();
                    newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
                });
        datePicker2.addValueChangeListener(
                e -> {
                    GroupItem selectedGroupItem = comboBox.getValue();
                    newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
                });

        comboBox.addValueChangeListener(
                e -> {
                    GroupItem selectedGroupItem = comboBox.getValue();
                    newItem = createNewItem(datePicker1.getValue(), datePicker2.getValue(), selectedGroupItem.getGroupId());
                });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(datePicker1, datePicker2, comboBox);

        addItemButton =
                new Button(
                        "Add Item",
                        e -> {
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

        timeline.addItemSelectListener(
                e -> {
                    timeline.onSelectItem(e.getTimeline(), e.getItemId(), bAutoZoom);
                    textField.setValue(e.getItemId());
                }
        );

        timeline.addGroupItemClickListener(e -> {
            StringBuilder temp = new StringBuilder();
            for (Item item : items) {
                if (Integer.parseInt(item.getGroupID()) == Integer.parseInt(e.getGroupId())) {
                    if (temp.length() > 0)
                        temp.append(",").append(item.getId());
                    else
                        temp.append(item.getId());

                }
            }
            e.getTimeline().onSelectItem(e.getTimeline(), temp.toString(), false);
        });


        return zoomOptionsLayout;
    }

    private Item createNewItem(LocalDateTime start, LocalDateTime end, String groupID) {
        if (start != null && end != null) {
            if (start.isBefore(end)) {
                addItemButton.setEnabled(true);
                return new Item(start, end, Integer.parseInt(groupID));
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

        return LitRenderer.<GroupItem>of("<span style= \"font-weight: ${item.width}; font-size: ${item.fontsize}\">${item.content}</span>")
                .withProperty("width", groupItem -> {
                    if (groupItem.getTreeLevel() == 1)
                        return "bolder";
                    else if (groupItem.getTreeLevel() == 2)
                        return "bold";
                    else
                        return "normal";
                })
                .withProperty("fontsize", groupItem -> {
                    if (groupItem.getTreeLevel() == 1)
                        return "1rem";
                    else if (groupItem.getTreeLevel() == 2)
                        return "0.9rem";
                    else
                        return "0.8rem";
                })
                .withProperty("content", GroupItem::getContent);
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