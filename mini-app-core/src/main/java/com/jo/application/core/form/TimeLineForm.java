package com.jo.application.core.form;

import com.jo.application.core.data.service.ZJTService;
import com.jo.application.data.entity.ZJTItem;
import com.jo.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.model.AxisOrientation;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CssImport(value = "./styles/timeline.css")
public abstract class TimeLineForm<S extends ZJTService> extends CommonForm {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    Timeline timeline;
    private final TimeLineViewParameter timeLineViewParameter;
    protected S service;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private final HorizontalLayout itemSummaryLayout = new HorizontalLayout();

    private final HorizontalLayout itemDateLayout = new HorizontalLayout();

    private String filteredValue = "";


    List<ZJTItem> entityItems;
    List<Item> items;
    List<ItemGroup> itemGroups;

    ComboBox<ZJTItem> itemComboBox = new ComboBox<>();
    ComboBox<ItemGroup> groupComboBox = new ComboBox<>();

    DateTimePicker datePickerStart = new DateTimePicker();

    DateTimePicker datePickerEnd = new DateTimePicker();

    public TimeLineForm(TimeLineViewParameter timeLineViewParameter,
                        S service) {
        this.timeLineViewParameter = timeLineViewParameter;
        this.service = service;
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
     *  Fillup contents of the toolbar
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

    private void configureItemSummary()
    {
        if (!timeLineViewParameter.isShowItemSelector()) {
            //don't do anything
            return;
        }

        groupComboBox.setItemLabelGenerator(ItemGroup::getContent);
        groupComboBox.setWidth("250px");

        itemComboBox.setItemLabelGenerator(ZJTItem::getTitle);
        itemComboBox.setWidth("750px");

        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.CHEVRON_DOWN));
        button.setTooltipText("Assign item to group");
        button.addClickListener(e -> updateItem());

        itemSummaryLayout.add(itemComboBox, groupComboBox, button);

        Button buttonDate = new Button();
        buttonDate.setIcon(new Icon(VaadinIcon.CHEVRON_DOWN));
        buttonDate.setTooltipText("Update item date range");
        buttonDate.addClickListener(e -> updateItem());

        itemDateLayout.add(datePickerStart, datePickerEnd, buttonDate);

        addComponentAtIndex(1, itemSummaryLayout);

        addComponentAtIndex(2, itemDateLayout);

        //TODO - add listeners
        timeline.addGroupItemClickListener(e-> {
//            int groupID = Integer.parseInt(e.getGroupId());
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

        timeline.addItemSelectListener(e-> {
            ZJTItem iSelected = null;
            for (ZJTItem item : entityItems) {
                if (item.getId() == Integer.parseInt(e.getItemId())) {
                    iSelected = item;
                    break;
                }
            }
            if (iSelected != null) {
                itemComboBox.setValue(iSelected);
                datePickerStart.setValue(iSelected.getStartTime());
                datePickerEnd.setValue(iSelected.getEndTime());
            }
        });

        groupComboBox.addValueChangeListener( e-> {
            if(groupComboBox.getValue() != null) {
                String groupId = groupComboBox.getValue().getGroupId() + "";
                timeline.setSelectGroup(groupId);
            }
        });

        itemComboBox.addValueChangeListener( e-> {
            if(itemComboBox.getValue() != null) {
                String itemId = itemComboBox.getValue().getId() + "";
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

        /*
        if (timeLineViewParameter.getParameters() != null
                && (int) timeLineViewParameter.getParameters()[0] != -1) {
            Object[] parameters = timeLineViewParameter.getParameters();
            if (parameters.length > 1 && parameters[1] != null) {
                query.append(" WHERE ").append("p.").append(timeLineViewParameter.getGroupClassPKField());
                //TODO - set parameter
                query.append(" = ").append(parameters[1]);
            } else {
                query.append(" WHERE ").append("p.").append(timeLineViewParameter.getGroupClassPKField());
                //TODO - set parameter
                query.append(" = ").append(parameters[0]);
            }
        }

         */

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

                query.append(" p.").append(where).append("=").append(timeLineViewParameter.getGroupParameters()[i]);
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

        timeline.setTimelineRange(LocalDateTime.of(2023, 1, 1, 0, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0, 0));

        //height must be specified otherwise timeline will just expand beyond screen size
        //TODO - must adjust based on screen resolution
        timeline.setHeight("800px");

        timeline.setMultiselect(true);
        timeline.setVerticalScroll(true);
        timeline.setAxisOrientation(AxisOrientation.BOTH);
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
            add(toolbar, timeline);
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

            TableData.add(item);
        }
        return TableData;
    }

    private String getStandardQuery(Object[] parameters) throws Exception {
        StringBuilder query = new StringBuilder("SELECT ");
        boolean  addAndStatement = false;
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

        //much check pairing of series
        for (String startDateFieldName : timeLineViewParameter.getStartDateFieldNames()) {
            query.append(", p.").append(startDateFieldName).append(" AS startDate").append(count);
            String endDateFieldName =  timeLineViewParameter.getEndDateFieldName()[count];
            query.append(", p.").append(endDateFieldName).append(" AS endDate").append(count);
            count++;
        }
//        query.append(timeLineViewParameter.getEndDateFieldName() != null ? ", p." + timeLineViewParameter.getEndDateFieldName() : "")
//                .append(timeLineViewParameter.getClassNameFieldName() != null ? ", p." + timeLineViewParameter.getClassNameFieldName() : "");

        query.append(" FROM ").append(timeLineViewParameter.getFromDefinition()).append(" as p");

//        if (timeLineViewParameter.getWhereDefinitions().length != parameters.length) {
//            throw new Exception("Size of where condition is not compatible to parameter");
//
//        }

        if (!ArrayUtils.isEmpty(timeLineViewParameter.getWhereDefinitions())
                && !ArrayUtils.isEmpty(parameters)
                && timeLineViewParameter.getWhereDefinitions().length == parameters.length) {
            int i = 0;
            for (String where : timeLineViewParameter.getWhereDefinitions()) {
                if (addWhereStatement) {
                    query.append(" WHERE ");
                    addWhereStatement = false;
                }
                if (addAndStatement) {
                    query.append(" AND ");
                }
                query.append(" p.").append(where).append("=").append(parameters[i]);
                i++;
                addAndStatement = true;
            }
        }
//        if (timeLineViewParameter.getWhereDefinitions() != null) {
//            query.append(" WHERE p.").append(timeLineViewParameter.getWhereDefinitions()[0]);
//            query.append(" = ").append(parameters[0]);
//            query.append(" AND p.").append(timeLineViewParameter.getWhereDefinitions()[1]);
//            query.append(" = ").append(parameters[2]);
//        } else if (timeLineViewParameter.getWhereDefinition() != null) {
//            query.append(" WHERE p.").append(timeLineViewParameter.getWhereDefinition());
//            //TODO -set parameter
//            query.append(" = ").append(parameters[0]);
////            if (dateFilterOn != null) {
////                addConditionWhenFilteringDate(query);
////            }
//        } else {
//
//        }

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
    public void onUpdateForm() throws Exception {
        if (this.timeLineViewParameter == null)
            return;

        itemGroups = this.getGroupItems(configureGroup());
        items = this.getItems(true);
        timeline.setGroups(itemGroups);
        timeline.setItems(items, true);

        if (timeLineViewParameter.isShowItemSelector()) {
            groupComboBox.setItems(itemGroups);
            itemComboBox.setItems(entityItems);
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
        ZJTItem item = itemComboBox.getValue();
        if (item != null) {
            ItemGroup group = groupComboBox.getValue();
            if(group != null) {
                item.setGroupId(String.valueOf(group.getGroupId()));
                timeline.updateItemGroup(String.valueOf(item.getId()), String.valueOf(group.getGroupId()));
                timeline.updateItemContent(String.valueOf(item.getId()), item.getTitle() + " " + String.valueOf(group.getGroupId()));
            }
            if(datePickerStart.getValue() != null && datePickerEnd.getValue() != null) {
                item.setStartTime(datePickerStart.getValue());
                item.setEndTime(datePickerEnd.getValue());
                timeline.revertMove(String.valueOf(item.getId()), item.getStartTime(), item.getEndTime());
            }
            getCommonView().onTimelineItemUpdate(item);
        }
    }
}