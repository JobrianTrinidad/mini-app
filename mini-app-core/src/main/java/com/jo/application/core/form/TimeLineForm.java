package com.jo.application.core.form;

import com.jo.application.core.data.service.ZJTService;
import com.jo.application.data.entity.ZJTItem;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.util.GlobalData;
import com.jo.application.util.ZoomUtil;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.context.ItemContextMenuEventHandler;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@CssImport(value = "./styles/timeline.css")
public abstract class TimeLineForm<S extends ZJTService> extends CommonForm {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    Timeline timeline;
    private final TimeLineViewParameter timeLineViewParameter;

    protected  TableInfoService tableInfoService;

    protected S service;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private final HorizontalLayout itemSummaryLayout = new HorizontalLayout();

    private final HorizontalLayout itemDateLayout = new HorizontalLayout();

    private String filteredValue = "";
    List<ZJTItem> entityItems;
    List<Item> items;
    List<ItemGroup> itemGroups;

    MultiSelectComboBox<ZJTItem> itemComboBox = new MultiSelectComboBox<>();
    ComboBox<ItemGroup> groupComboBox = new ComboBox<>();

    DateTimePicker datePickerStart = new DateTimePicker();

    DateTimePicker datePickerEnd = new DateTimePicker();

    public TimeLineForm(TimeLineViewParameter timeLineViewParameter,
                        S service, TableInfoService tableInfoService) {
        this.timeLineViewParameter = timeLineViewParameter;
        this.service = service;
        this.tableInfoService = tableInfoService;
        dateFilterOn = timeLineViewParameter.getDateFilterOn();
        addClassName("mobile-app-form");
        configureTimeLine();
        try {
            configureToolbar();
            addComponentAtIndex(0, toolbar);
            configureItemSummary();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fillup contents of the toolbar
     *
     * @throws Exception
     */
    private void configureToolbar() throws Exception {
        VerticalLayout itemKindLayout = new VerticalLayout();

        int nStartDateId = 0;
        int seriesSize = timeLineViewParameter.getStartDateFieldNames().length;
        //no point of displaying legend when there is only one
        if (seriesSize > 1) {
            for (String fieldName : timeLineViewParameter.getStartDateFieldNames()) {
                HorizontalLayout everyItemLayout = new HorizontalLayout();
                everyItemLayout.setWidth(200, Unit.PIXELS);
                everyItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

                Span label = new Span(GlobalData.convertToStandard(fieldName));
                Div graph = new Div();
                graph.setWidth(50, Unit.PIXELS);
                //TODO - this needs review, class is hard coded and not meaningful
                switch (nStartDateId) {
                    case 0:
                        graph.setClassName("bg-success");
                        break;
                    case 1:
                        graph.setClassName("bg-warning");
                        break;
                    default:
                        graph.setClassName("bg-error");
                        break;
                }
                everyItemLayout.add(label, graph);
                itemKindLayout.add(everyItemLayout);
                nStartDateId++;
            }
            toolbar.add(itemKindLayout);

        }

        /*
        if (this.timeLineViewParameter.getParameters() != null &&
                (int) this.timeLineViewParameter.getParameters()[0] != -1) {
            if (!timeLineViewParameter.isValid()) {
                throw new Exception("TuiGrid Definition is not valid.");
            }
            if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getSelectDefinition() == null) {
                throw new Exception("Parameters are required, but not set");
            }

            StringBuilder query = new StringBuilder("SELECT p.").append(timeLineViewParameter.getGroupSelectDefinition());

            query.append(" FROM ").append(timeLineViewParameter.getGroupClass().getSimpleName()).append(" as p");

            if (timeLineViewParameter.getWhereDefinition() != null && (int) timeLineViewParameter.getParameters()[0] != -1) {
                String[] whereDefinition = timeLineViewParameter.getWhereDefinition().split("\\.");
                switch (timeLineViewParameter.getWhereDefinition().split("\\.").length) {
                    case 2:
                        query.append(" WHERE ").append("p.").append(timeLineViewParameter.getWhereDefinition());
                        break;
                    case 3:
                        query.append(" WHERE ").append("p.").append(whereDefinition[1]).append(".").append(whereDefinition[2]);
                        break;
                }
                //TODO -set parameter
                query.append(" = ").append(timeLineViewParameter.getParameters()[0]);
            }

                filteredValue = String.valueOf(service.findEntityByQuery(query.toString()).get(0));
        }

         */
        if (dateFilterOn != null)
            toolbar.add(dateFilter);
    }

    private void configureItemSummary() {
        if (!timeLineViewParameter.isShowItemSelector()) {
            //don't do anything
            return;
        }

        groupComboBox.setItemLabelGenerator(ItemGroup::getContent);
        groupComboBox.setWidth("200px");

        itemComboBox.setItemLabelGenerator(ZJTItem::getTitle);
        itemComboBox.setWidth("600px");

        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.CHEVRON_DOWN));
        button.setTooltipText("Assign item to group");
        button.addClickListener(e -> updateItemGroup());

        itemSummaryLayout.add(itemComboBox, groupComboBox, datePickerStart, datePickerEnd,  button);

//        update within updateItemGroup
//        Button buttonDate = new Button();
//        buttonDate.setIcon(new Icon(VaadinIcon.CHEVRON_DOWN));
//        buttonDate.setTooltipText("Update item date range");
//        buttonDate.addClickListener(e -> updateItem());

//        itemDateLayout.add(datePickerStart, datePickerEnd, buttonDate);

        addComponentAtIndex(1, itemSummaryLayout);

//        addComponentAtIndex(2, itemDateLayout);

        // add listeners
        timeline.addGroupClickListener(e -> {
            ItemGroup gSelected = null;
            for (ItemGroup group : itemGroups) {
                if (group.getGroupId() == Integer.parseInt(e.getGroupId())) {
                    gSelected = group;
                    break;
                }
            }
            if (gSelected != null && !gSelected.equals(groupComboBox.getValue())) {
                groupComboBox.setValue(gSelected);
            }
        });

        ItemContextMenuEventHandler eventHandler = new ItemContextMenuEventHandler();
        if (this.timeLineViewParameter.getItemContextMenuEventHandler() != null) {
            eventHandler = this.timeLineViewParameter.getItemContextMenuEventHandler();
        }
        eventHandler.setGroupZoomable(this.timeLineViewParameter.getGroupZoomTableID() > 0);
        timeline.setTimeLineContextHandler(eventHandler);

        timeline.addGroupItemsSelectListener((event) -> {
            StringBuilder temp = new StringBuilder();
            List<ZJTItem> selectItems = new ArrayList<>();
            for (ZJTItem item : entityItems) {
                String itemGroup = item.getGroupId();
                boolean isSelectedGroup = event.getSelectedGroupList().stream()
                        .anyMatch(group -> String.valueOf(group.getGroupId()).equalsIgnoreCase(itemGroup));
                if (isSelectedGroup) {
                    if (!temp.isEmpty())
                        temp.append(",").append(item.getId());
                    else
                        temp.append(item.getId());
                    selectItems.add(item);
                }
            }
            itemComboBox.setValue(selectItems);
            event.getTimeline().onSelectItem(event.getTimeline(), temp.toString(), false);
        });

        timeline.addGroupZoomListener((event) -> {
                ZoomUtil.generateAndOpenUrl(this.timeLineViewParameter.getGroupZoomTableID(), event.getGroupId());
        });

        if (this.timeLineViewParameter.getContextFormEventHandler() != null)
            timeline.addItemContextForm(this.timeLineViewParameter.getContextFormEventHandler());

        timeline.addItemSelectListener(e -> {
            List<String> selecteItems = Arrays.asList(e.getItemId().split(","));
            List<ZJTItem> iSelectedItems = new ArrayList<>();
            HashSet<String> gSelectedItems = new HashSet<String>();
            for (ZJTItem item : entityItems) {
                if (selecteItems.contains(String.valueOf(item.getId()))) {
                    iSelectedItems.add(item);
                    gSelectedItems.add(item.getGroupId());
                }
            }
            if (!iSelectedItems.isEmpty()) {
                itemComboBox.setValue(iSelectedItems);
                if (gSelectedItems.size() == 1) {
                    datePickerStart.setValue(iSelectedItems.get(0).getStartTime());
                    datePickerEnd.setValue(iSelectedItems.get(0).getEndTime());
                    if (groupComboBox.getValue() != null && groupComboBox.getValue().getGroupId() != Integer.parseInt(iSelectedItems.get(0).getGroupId()))
                        groupComboBox.setValue(null);
                } else {
                    datePickerStart.setValue(null);
                    datePickerEnd.setValue(null);
                    groupComboBox.setValue(null);
                }
            }
        });

        groupComboBox.addValueChangeListener(e -> {
            if (groupComboBox.getValue() != null) {
                String groupId = groupComboBox.getValue().getGroupId() + "";
                timeline.setSelectGroup(groupId);
            }
        });

        itemComboBox.addValueChangeListener(e -> {
            if (itemComboBox.getValue() != null) {
                String itemId = itemComboBox.getValue().stream()
                        .filter(Objects::nonNull) // Filter out null items
                        .map(item -> String.valueOf(item.getId()))
                        .collect(Collectors.joining(","));
                timeline.onSelectItem(timeline, itemId, true);
            }
        });


    }

    private List<Object[]> configureGroup() throws Exception {
        if (!timeLineViewParameter.isValid()) {
            throw new Exception("Timeline parameter Definition is not valid.");
        }
//        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
//            throw new Exception("Parameters are required, but not set");
//        }

        StringBuilder query = new StringBuilder("SELECT p.")
                .append(timeLineViewParameter.getGroupClassPKField())
                .append(", p.").append(timeLineViewParameter.getGroupSelectDefinition());


        if (timeLineViewParameter.getGroupCSSClass() == null) {
            query.append(", null ");
        } else {
            query.append(", p.").append(timeLineViewParameter.getGroupCSSClass());
        }
        query.append(" FROM ").append(timeLineViewParameter.getGroupClass().getSimpleName()).append(" as p");

        boolean addWhereStatement = true;
        boolean addAndStatement = false;
        int i = 0;
        if (!ArrayUtils.isEmpty(timeLineViewParameter.getGroupWhereDefinitions())
                && !ArrayUtils.isEmpty(timeLineViewParameter.getGroupParameters())
                && timeLineViewParameter.getGroupParameters().length == timeLineViewParameter.getGroupWhereDefinitions().length
        ) {
            for (String where : timeLineViewParameter.getGroupWhereDefinitions()) {
                if (addWhereStatement) {
                    query.append(" WHERE ");
                    addWhereStatement = false;
                }
                if (addAndStatement) {
                    query.append(" AND ");
                }

                if (where.toUpperCase().contains("OR")) {
                    if(timeLineViewParameter.getGroupParameters()[i] instanceof List<?>) {
                        List<?> parameters = (List<?>) timeLineViewParameter.getGroupParameters()[i];
                        // Convert the list to a comma-separated string of values
                        String inClause = parameters.stream()
                                .map(Object::toString) // Convert each item to a string
                                .collect(Collectors.joining(", ", "(", ")")); // Format with parentheses

                        query.append ("(").append(where).append(" IN ").append(inClause).append(")");
                    }   else {
                        query.append ("(").append(where).append("=").append(timeLineViewParameter.getGroupParameters()[i]).append(")");
                    }
                } else {
                    if(timeLineViewParameter.getGroupParameters()[i] instanceof List<?>)
                    {
                        List<?> parameters = (List<?>) timeLineViewParameter.getGroupParameters()[i];
                        // Convert the list to a comma-separated string of values
                        String inClause = parameters.stream()
                                .map(Object::toString) // Convert each item to a string
                                .collect(Collectors.joining(", ", "(", ")")); // Format with parentheses
                        // Append the IN clause to the query
                        query.append(" p.").append(where).append(" IN ").append(inClause);
                    }
                    else {
                        query.append(" p.").append(where).append("=").append(timeLineViewParameter.getGroupParameters()[i]);
                    }
                }

                i++;
                addAndStatement = true;
            }
        }
        query.append(" ORDER BY ").append(timeLineViewParameter.getGroupSelectDefinition());
        return service.findEntityByQuery(query.toString());
    }

    private void configureTimeLine() {

        try {
            items = getItems(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            itemGroups = getGroupItems(configureGroup());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (itemGroups == null)
            timeline = new Timeline(items);
        else
            timeline = new Timeline(items, itemGroups);

//        timeline.setTimelineRange(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2025, 12, 25, 0, 0, 0));

        //height must be specified otherwise timeline will just expand beyond screen size
        //TODO - must adjust based on screen resolution
        timeline.setHeight("840px");

        timeline.setMultiselect(true);
        timeline.setVerticalScroll(true);
        timeline.setAxisOrientation(this.timeLineViewParameter.getAxisOrientation());
        timeline.setStack(this.timeLineViewParameter.isStack());
        // TODO: Uncomment to use the default logic of timeline to stack the sub-group
//        timeline.setStackSubgroups(this.timeLineViewParameter.isStackSubgroups());
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
            add(toolbar, timeline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        timeline.addWindowRangeChangedListener(event -> {
            if (!endDatePicker.getValue().isEqual(event.getNewEnd().toLocalDate()))
                endDatePicker.setValue(event.getNewEnd().toLocalDate());
            if (!(startDatePicker.getValue().isEqual(event.getNewStart().plusDays(1).toLocalDate()) || startDatePicker.getValue().isEqual(event.getNewStart().toLocalDate())))
                startDatePicker.setValue(event.getNewStart().toLocalDate());
        });
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
//        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
//            throw new Exception("Parameters are required, but not set");
//        }

        String query = getStandardQuery(timeLineViewParameter.getParameters());

        entityItems = service.findByQuery(query);

        for (ZJTItem data : entityItems) {
            Item item = new Item();
            item.setId(data.getId() + "");
            item.setContent(data.getTitle());
            item.setTitle(data.getTitle());
            item.setClassName(data.getClassName());
            item.setStyle(data.getStyle());

//TODO must assign new class base on series/startdateid (e.g. actual vs plan)
//            switch (data.getStartDateId()) {
//                case 0:
//                    item.setClassName("bg-success");
//                    break;
//                case 1:
//                    item.setClassName("bg-warning");
//                    break;
//                default:
//                    item.setClassName("bg-error");
//                    break;
//            }

            item.setStart(data.getStartTime());
            item.setEnd(data.getEndTime());
            item.setGroup(data.getGroupId());
            item.setSubgroup(data.getSubgroupId());
            item.setSubgroupOrder(StringUtils.isEmpty(data.getSubgroupId())? 0 : Integer.parseInt(data.getSubgroupId()));
            TableData.add(item);
        }
        return TableData;
    }

    private String getStandardQuery(Object[] parameters) throws Exception {
        StringBuilder query = new StringBuilder("SELECT ");
        boolean addAndStatement = false;
        boolean addWhereStatement = true;

        //ID must be mandatory - otherwise you'll not be able to identify the item
        query.append("p.").append(timeLineViewParameter.getIdFieldName()).append(" as id");
        query.append(", CONCAT(p.").append(timeLineViewParameter.getTitleFieldName()[0]);
        int index = 0;
        for (String titleField : timeLineViewParameter.getTitleFieldName()) {
            if (index != 0)
                query.append(", ' '").append(", p.").append(titleField);
            index++;
        }
        query.append(") AS title");
        int count = 0;
        query.append(", p.").append(timeLineViewParameter.getGroupIDFieldName()).append(" AS groupId");

        //pass item classname or style
        if (timeLineViewParameter.getClassNameFieldName() == null) {
            query.append(", '' AS classname");
        } else {
            query.append(", p.").append(timeLineViewParameter.getClassNameFieldName()).append(" AS classname");
        }

        if (timeLineViewParameter.getItemBorderClassField() == null) {
            query.append(", '' AS borderstyle");
        } else {
            query.append(", p.").append(timeLineViewParameter.getItemBorderClassField()).append(" AS borderstyle");
        }

        if (timeLineViewParameter.getItemEditableField() == null) {
            if (timeLineViewParameter.isEditableFlagInverted()) {
                query.append(", 0 AS editable");
            } else {
                query.append(", 1 AS editable");
            }
        } else {
            if (timeLineViewParameter.isEditableFlagInverted()) {
                query.append(", case when p.").append(timeLineViewParameter.getItemEditableField()).append(" then 0 else 1 end AS editable");
            } else {
                query.append(", case when p.").append(timeLineViewParameter.getItemEditableField()).append(" then 1 else 0 end AS editable");
            }
        }

        if (timeLineViewParameter.getSubgroupIDFieldName() == null) {
            query.append(", '' AS subgroup");
        } else {
            query.append(", p.").append(timeLineViewParameter.getSubgroupIDFieldName()).append(" AS subgroup");
        }
        //much check pairing of series
        for (String startDateFieldName : timeLineViewParameter.getStartDateFieldNames()) {
            query.append(", p.").append(startDateFieldName).append(" AS startDate").append(count);
            String endDateFieldName = timeLineViewParameter.getEndDateFieldName()[count];
            query.append(", p.").append(endDateFieldName).append(" AS endDate").append(count);
            count++;
        }

        query.append(" FROM ").append(timeLineViewParameter.getFromDefinition()).append(" as p");


        if (!ArrayUtils.isEmpty(timeLineViewParameter.getWhereDefinitions())
                && !ArrayUtils.isEmpty(parameters)
                && timeLineViewParameter.getWhereDefinitions().length == parameters.length) {
            int i = 0;
            boolean addClosingBracket = false;
            for (String where : timeLineViewParameter.getWhereDefinitions()) {

                if (parameters[i] == null) {
                    i++;
                    continue;
                }

                if (addWhereStatement) {
                    query.append(" WHERE ");
                    addWhereStatement = false;
                }
                if (addAndStatement) {
                    query.append(" AND ");
                }

                //add support for OR statement - no support for parameter
                //where would be something like this - must pass alias p explicitly
                // parameter -->  p.resource_id < 100 or p.filter_id
                // result -->  (p.resource_id < 100 or p.filter_id IN (1, 2))
                if (where.toUpperCase().contains("OR")) {
                    if(parameters[i] instanceof List<?>) {
                        List<?> lparameters = (List<?>) parameters[i];
                        // Convert the list to a comma-separated string of values
                        String inClause = lparameters.stream()
                                .map(Object::toString) // Convert each item to a string
                                .collect(Collectors.joining(", ", "(", ")")); // Format with parentheses
                        query.append ("(").append(where).append(" IN ").append(inClause).append(")");
                    } else {
                        query.append ("(").append(where).append("=").append(parameters[i]).append(")");
                    }
                } else {
                    if(parameters[i] instanceof List<?>)
                    {
                        List<?> lparameters = (List<?>) parameters[i];
                        // Convert the list to a comma-separated string of values
                        String inClause = lparameters.stream()
                                .map(Object::toString) // Convert each item to a string
                                .collect(Collectors.joining(", ", "(", ")")); // Format with parentheses
                        // Append the IN clause to the query
                        query.append(" p.").append(where).append(" IN ").append(inClause);
                    } else {
                        query.append(" p.").append(where).append("=").append(parameters[i]);
                    }
                }

                i++;
                addAndStatement = true;
            }
        }

        if (dateFilterOn != null) {
            if (addWhereStatement) {
                query.append(" WHERE ");
                addWhereStatement = false;
            }
            if (addAndStatement) {
                query.append(" AND ");
            }
            addConditionWhenFilteringDate(query);
        }
        return query.toString();
    }

    private List<ItemGroup> getGroupItems(List<Object[]> groupResults) {
        if (groupResults == null)
            return null;
        List<ItemGroup> itemGroups = new ArrayList<>();
        for (Object[] groupResult : groupResults) {
            ItemGroup itemGroup = new ItemGroup();
            itemGroup.setTreeLevel(0);
            itemGroup.setNestedGroups(null);
            itemGroup.setClassName((String) groupResult[2]);
            itemGroup.setVisible(true);
            // TODO: This will run the custom logic for the sub-group stack.
            itemGroup.setSubgroupStack(this.timeLineViewParameter.isStackSubgroups());
            itemGroup.setId((Integer) groupResult[0]);
            itemGroup.setContent((String) groupResult[1]);

            itemGroups.add(itemGroup);
        }
        return itemGroups;
    }

    @Override
    public String getHamburgerText() {
        if (filteredValue.isEmpty()) {
            return "";
        }
        return ">> " + timeLineViewParameter.getPageName() + filteredValue;
    }

    @Override
    public String getOriginViewText() {
        return GlobalData.convertToStandard(this.timeLineViewParameter.groupName);
//        return "";
    }

    @Override
    public void onUpdateTimeWindow() throws Exception {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (timeline != null && start != null && end != null && (start.isEqual(end) || start.isBefore(end))) {
            if (start.isEqual(end))
                timeline.moveWindowTo(start.atStartOfDay(), LocalDateTime.of(end, LocalTime.MAX));
            else
                timeline.moveWindowTo(start.atStartOfDay(), end.atStartOfDay());
        }
    }

    @Override
    public void onUpdateForm() throws Exception {
        if (this.timeLineViewParameter == null || timeline == null)
            return;

        itemGroups = this.getGroupItems(configureGroup());
        items = this.getItems(true);
        timeline.setGroups(itemGroups);
        timeline.setItems(items, true);

        if (timeLineViewParameter.isShowItemSelector()) {
            groupComboBox.setItems(itemGroups);
            itemComboBox.setItems(entityItems);
        }

        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start != null && end != null) {
            timeline.setStart(start.atStartOfDay());
            timeline.setEnd(end.atTime(LocalTime.MAX));
        }
        if (timeLineViewParameter.getContextFormEventHandler() != null) {
            timeLineViewParameter.getContextFormEventHandler().setZjtItems(entityItems);
            timeLineViewParameter.getContextFormEventHandler().setItemGroups(itemGroups);
        }
    }

    public HorizontalLayout getToolbar() {
        return toolbar;
    }

    /**
     * Update item and save it to the database - could be overriden from the View
     * for finer control
     */
    public void updateItem()
    {
        Set<ZJTItem> items = itemComboBox.getValue();
        for(ZJTItem item : items) {
            if (item != null && datePickerStart.getValue() != null && datePickerEnd.getValue() != null) {
                item.setStartTime(datePickerStart.getValue());
                item.setEndTime(datePickerEnd.getValue());
                timeline.revertMove(String.valueOf(item.getId()), item.getStartTime(), item.getEndTime());
//redundant as it's being referenced on by updateItemGroup
//                getCommonView().onTimelineItemUpdate(item, items.size() > 1);
            }
        }
    }

    public void updateItemGroup()
    {
        Set<ZJTItem> items = itemComboBox.getValue();
        for(ZJTItem item : items) {
            if (item != null) {
                if (!item.isEditable()) {continue;}
                ItemGroup group = groupComboBox.getValue();
                if (group != null) {
                    item.setGroupId(String.valueOf(group.getGroupId()));
                    timeline.updateItemGroup(String.valueOf(item.getId()), String.valueOf(group.getGroupId()));
                    timeline.updateItemContent(String.valueOf(item.getId()), item.getTitle() + " " + String.valueOf(group.getGroupId()));
                    getCommonView().onTimelineItemUpdate(item, items.size() > 1);
                }
            }
        }

        if (items.size() == 1) {
            updateItem();
        }
        itemComboBox.deselectAll();
    }

    public List<ZJTItem> getEntityItems() {
        return entityItems;
    }

    public void setEntityItems(List<ZJTItem> entityItems) {
        this.entityItems = entityItems;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(List<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }
}