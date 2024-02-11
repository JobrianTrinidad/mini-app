package com.aat.application.core.form;

import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.Timeline;
import com.vaadin.componentfactory.timeline.model.Item;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class TimeLineForm<S extends ZJTService> extends CommonForm {

    @Serial
    private static final long serialVersionUID = -5183438338263448740L;
    Timeline timeline;
    private final TimeLineViewParameter timeLineViewParameter;
    protected S service;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private String filteredValue = "";

    private final DatePicker startDatePicker = new DatePicker("");
    private final DatePicker endDatePicker = new DatePicker("");
    private final ComboBox<EnumDateFilter> dateFilterComboBox = new ComboBox<>("");

    public TimeLineForm(TimeLineViewParameter timeLineViewParameter,
                        S service) {
        this.timeLineViewParameter = timeLineViewParameter;
        this.service = service;
        addClassName("demo-app-form");
        configureTimeLine();
        try {
            getToolbar();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addComponentAtIndex(0, toolbar);
    }

    private void getToolbar() throws Exception {
        VerticalLayout itemKindLayout = new VerticalLayout();

        int nStartDateId = 0;
        for (String fieldName : timeLineViewParameter.getStartDateFieldNames()) {
            HorizontalLayout everyItemLayout = new HorizontalLayout();
            everyItemLayout.setWidth(200, Unit.PIXELS);
            everyItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            Span label = new Span(GlobalData.convertToStandard(fieldName));
            Div graph = new Div();
            graph.setWidth(50, Unit.PIXELS);
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

        startDatePicker.addValueChangeListener(e -> {
            try {
                this.filterByDate(e.getValue().atStartOfDay(), null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        endDatePicker.addValueChangeListener(e -> {
            try {
                this.filterByDate(e.getValue().atStartOfDay(), null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        dateFilterComboBox.setItems(EnumDateFilter.values());
        dateFilterComboBox.addValueChangeListener(e -> updateDateFilter());
        dateFilterComboBox.setValue(EnumDateFilter.TM);
        HorizontalLayout dateFilter = new HorizontalLayout(dateFilterComboBox, startDatePicker, new Span("To"), endDatePicker);
        dateFilter.setAlignItems(FlexComponent.Alignment.CENTER);

        if (this.timeLineViewParameter.getParameters() != null &&
                (int) this.timeLineViewParameter.getParameters()[0] != -1) {
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
        if (timeLineViewParameter.getDateFilterOn() != null)
            //            toolbar.add(filterText, btnReload, btnSave, dateFilter);
            toolbar.add(dateFilter);
    }

    private List<Object[]> configureGroup() throws Exception {
        if (!timeLineViewParameter.isValid()) {
            throw new Exception("TuiGrid Definition is not valid.");
        }
        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
            throw new Exception("Parameters are required, but not set");
        }


        StringBuilder query = new StringBuilder("SELECT p.")
                .append(timeLineViewParameter.getGroupClassPKField())
                .append(", p.").append(timeLineViewParameter.getSelectDefinition());


        query.append(" FROM ").append(timeLineViewParameter.getGroupClass().getSimpleName()).append(" as p");

        if ((int) timeLineViewParameter.getParameters()[0] != -1) {
            query.append(" WHERE ").append("p.").append(timeLineViewParameter.getGroupClassPKField());
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
        if (timeLineViewParameter.isRequireParameter() && timeLineViewParameter.getParameters() == null) {
            throw new Exception("Parameters are required, but not set");
        }

        String query = getStandardQuery(timeLineViewParameter.getParameters());

        for (ZJTItem data :
                service.findByQuery(query)) {
            Item item = new Item();
//            item.setId(data.getId().toString());
            item.setContent(data.getTitle());

            switch (data.getStartDateId()) {
                case 0:
                    item.setClassName("bg-success");
                    break;
                case 1:
                    item.setClassName("bg-warning");
                    break;
                default:
                    item.setClassName("bg-error");
                    break;
            }

            item.setStart(data.getStartTime());
            item.setEnd(data.getEndTime());
            item.setGroup(data.getGroupId());

            TableData.add(item);
        }
        return TableData;
    }

    private String getStandardQuery(Object[] parameters) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append("CONCAT(p.").append(timeLineViewParameter.getTitleFieldName()[0]);
        int index = 0;
        for (String titleField : timeLineViewParameter.getTitleFieldName()) {
            if (index != 0)
                query.append(", ' '").append(", p.").append(titleField);
            index++;
        }
        query.append(") AS title");
        int count = 0;
        query.append(", p.").append(timeLineViewParameter.getGroupIDFieldName()).append(" AS groupId");
        for (String startDateFieldName : timeLineViewParameter.getStartDateFieldNames()) {
            query.append(", p.").append(startDateFieldName).append(" AS startDate").append(count);
            count++;
        }
        query.append(timeLineViewParameter.getEndDateFieldName() != null ? ", p." + timeLineViewParameter.getEndDateFieldName() : "")
                .append(timeLineViewParameter.getClassNameFieldName() != null ? ", p." + timeLineViewParameter.getClassNameFieldName() : "");

        query.append(" FROM ").append(timeLineViewParameter.getFromDefinition()).append(" as p");

        if (timeLineViewParameter.getWhereDefinition() != null) {
            query.append(" WHERE p.").append(timeLineViewParameter.getWhereDefinition());
            //TODO -set parameter
            query.append(" = ").append(parameters[0]);
            if (timeLineViewParameter.getDateFilterOn() != null) {
                addConditionWhenFilteringDate(query);
            }
        } else {
            if (timeLineViewParameter.getDateFilterOn() != null) {
                query.append(" WHERE 1=1");
                addConditionWhenFilteringDate(query);
            }
        }
        return query.toString();
    }

    private void addConditionWhenFilteringDate(StringBuilder query) {
        if (startDatePicker.getValue() != null
                && endDatePicker.getValue() != null) {
            if (!startDatePicker.getValue().equals(endDatePicker.getValue())) {
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            } else {
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            }

        } else {
            if (startDatePicker.getValue() != null)
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
            if (endDatePicker.getValue() != null)
                query.append(" AND DATE(p.").append(timeLineViewParameter.getDateFilterOn())
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
        }
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
        if (filteredValue.isEmpty()) {
            return "";
        }
        return ">> " + timeLineViewParameter.getPageName() + filteredValue;
    }

    @Override
    public String getOriginViewText() {
//        return GlobalData.convertToStandard(this.timeLineViewParameter.groupName);
        return "";
    }

    private void filterByDate(LocalDateTime start, LocalDateTime end) throws Exception {
        if (start != null && end != null) {
            if (!start.isBefore(end)) {
                Notification.show("End date should be after start date", 5000, Notification.Position.MIDDLE);
                return;
            }
        }
        timeline.setItems(this.getItems(true), true);
    }

    private void updateDateFilter() {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        switch (dateFilterComboBox.getValue()) {
            case TD:
                dateFrom = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                dateTo = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                break;

            case TW:
                dateFrom = LocalDate.now();
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case NW:
                dateFrom = LocalDate.now().plusWeeks(1);
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case TM:
                dateFrom = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
                dateTo = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()).atEndOfMonth();
                break;
            case NM:
                dateFrom = LocalDate.now().plusMonths(1);
                dateFrom = LocalDate.of(dateFrom.getYear(), dateFrom.getMonth(), 1);
                dateTo = YearMonth.of(dateFrom.getYear(), dateFrom.getMonth()).atEndOfMonth();

                break;


        }

        if (dateFrom != null)
            startDatePicker.setValue(dateFrom);
        if (dateTo != null)
            endDatePicker.setValue(dateTo);
    }
}