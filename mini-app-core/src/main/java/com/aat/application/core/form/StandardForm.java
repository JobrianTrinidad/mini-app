package com.aat.application.core.form;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.core.component.AATTwinColSelect;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTTableInfo;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;

import java.io.Serial;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends CommonForm<T> {

    @Serial
    private static final long serialVersionUID = -5183438338263448739L;
    private GridViewParameter gridViewParameter;

    protected TableInfoService tableInfoService;
    String fieldDisplayedInSelect;
    private ZJTTableInfo tableInfo;
    protected TextField filterText = new TextField();
    private final Button btnReload = new Button("Reload");
    private final Button btnSave = new Button("Save");
    protected Button columns;
    private Dialog twinColSelDialog;
    AATTwinColSelect twinColSelect;
    Set<String> selectedItems;
    public TuiGrid grid;
    protected S service;
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();
    private boolean bSavedWidth = false;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private HorizontalLayout routeLayout = new HorizontalLayout();

    private final HorizontalLayout statusBar = new HorizontalLayout();
    private final Button btnInfo = new Button();
    private final TextField lblMessage = new TextField();
    private final Button lblRowCount = new Button();

    public StandardForm(GridViewParameter gridViewParameter,
                        S service, TableInfoService tableInfoService) {
        addClassName("demo-app-form");
        this.gridViewParameter = gridViewParameter;
        this.service = service;
        this.tableInfoService = tableInfoService;
        this.setHeight("calc(100vh - 60px)");

        initColSelDialog();

        add(routeLayout, toolbar);
        loadGrid();

        addStatusBar();
    }

    private void addStatusBar() {

        HorizontalLayout left = new HorizontalLayout();
        btnInfo.setIcon(new Icon(VaadinIcon.INFO));
        left.setWidthFull();
        left.add(lblMessage);
        lblMessage.setWidthFull();

        statusBar.add(btnInfo, left, lblRowCount);
        statusBar.setHeight("40px");
        statusBar.setWidthFull();

        lblMessage.setValue("OK");
        lblRowCount.setText("#");
        lblRowCount.setId("rowcount");
        btnInfo.addClickListener(e -> showMessageInDialog());

        add(statusBar);
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        grid.setRowCountOnElement("rowcount");
    }


    private void showMessageInDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Status Message");
        dialog.setText(lblMessage.getValue());

        dialog.open();

    }

    private void loadGrid() {
//        removeAll();
        if (!twinColSelect.getSelectedItems().isEmpty())
            configureGrid();
        getToolbar();
        if (grid != null)
            add(grid);
    }

    private void initColSelDialog() {
        twinColSelect = new AATTwinColSelect();
        List<String> tempHeaderNames = new ArrayList<>();
        for (String header : this.gridViewParameter.getHeaders()) {
            tempHeaderNames.add(this.gridViewParameter.headerNames.get(header));
        }

        twinColSelect.setItems(tempHeaderNames);

        tableInfo = tableInfoService.findByTableName(this.gridViewParameter.getEntityClass().getSimpleName());
        if (tableInfo == null) {
            tableInfo = new ZJTTableInfo();
            tableInfo.setTable_name(this.gridViewParameter.getEntityClass().getSimpleName());
        }
        String tempHeader = tableInfo.getHeaders();
        List<String> tempDisplayedHeaderNames = tempHeaderNames;
        if (tempHeader != null && !tempHeader.equals("[]")) {
            this.gridViewParameter.setHeaders(
                    Arrays.stream(tempHeader.substring(1, tempHeader.length() - 1).split(","))
                            .map(String::trim).collect(Collectors.toList()));
            tempDisplayedHeaderNames = new ArrayList<>();
            for (String header : this.gridViewParameter.getHeaders()) {
                tempDisplayedHeaderNames.add(this.gridViewParameter.getHeaderNames().get(header));
            }
        }

        twinColSelect.select(tempDisplayedHeaderNames);


        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");
        HorizontalLayout btnPanel = new HorizontalLayout(btnCancel, btnOk);
        btnPanel.setAlignItems(FlexComponent.Alignment.END);

        ZJTTableInfo finalTableInfo = tableInfo;
        btnOk.addClickListener(e -> {
            List<String> originHeaders = this.gridViewParameter.getHeaders();
            List<Integer> columnWidths = getColumnWidths();
            List<Integer> allowedWidths = new ArrayList<>();
            List<String> tempTwinItems = new ArrayList<>(twinColSelect.getSelectedItems());
            List<String> tempHeaders = new ArrayList<>();
            if (!tempTwinItems.contains(fieldDisplayedInSelect)
                    && fieldDisplayedInSelect != null)
                tempTwinItems.add(fieldDisplayedInSelect);
            twinColSelect.select(tempTwinItems);
            for (String desiredValue : tempTwinItems) {
                Enumeration<String> keys = this.gridViewParameter.getHeaderNames().keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();

                    if (this.gridViewParameter.getHeaderNames().get(key).equals(desiredValue)) {
                        tempHeaders.add(key);
                        break;
                    }
                }
            }
            this.gridViewParameter.setHeaders(tempHeaders);

            for (String allowedHeader : this.gridViewParameter.getHeaders()) {
                if (originHeaders.contains(allowedHeader)) {
                    allowedWidths.add(columnWidths.get(originHeaders.indexOf(allowedHeader)));
                } else allowedWidths.add(0);
            }

            finalTableInfo.setWidths(allowedWidths.toString());
            finalTableInfo.setHeaders(this.gridViewParameter.getHeaders().toString());
            tableInfoService.save(finalTableInfo);
            tableInfo = finalTableInfo;

            if (!this.gridViewParameter.getHeaders().isEmpty())
                this.loadGrid();
            else grid.removeFromParent();

            twinColSelDialog.close();
        });

        btnCancel.addClickListener(e -> twinColSelDialog.close());

        twinColSelDialog = new Dialog();
        twinColSelDialog.add(new VerticalLayout(twinColSelect, btnPanel));
        twinColSelDialog.setCloseOnEsc(true);
        twinColSelDialog.setCloseOnOutsideClick(true);
    }

    private void configureGrid() {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(this.gridViewParameter.getHeaders());

        try {
            items = this.getTableData(this.gridViewParameter.getParameters(), false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        grid.setItems(items);

        List<Column> columns = this.getColumns();
        grid.setColumns(columns);

//        List<Summary> summaries = List.of(
//                new Summary(columns.get(columns.size() - 1).getColumnBaseOption().getName(), Summary.OperationType.rowcount));

//        grid.setSummaries(summaries);
        grid.setHeaderHeight(100);
//        grid.setSummaryHeight(40);

        grid.setRowHeaders(List.of("checkbox"));
        grid.sethScroll(true);
        grid.setvScroll(true);

        Theme inputTheme = new Theme();
        inputTheme.setBorder("1px solid #326f70");
        inputTheme.setBackgroundColor("#66878858");
        inputTheme.setOutline("none");
        inputTheme.setWidth("90%");
        inputTheme.setHeight("100%");
        inputTheme.setOpacity(1);

        grid.setInputTheme(inputTheme);
        grid.setSelectTheme(inputTheme);

        grid.addColumnResizeListener(event -> {
            int colWidth = event.getColWidth();
            String colName = event.getColName();
            List<Integer> colWidths = getColumnWidths();

            for (String header : this.gridViewParameter.getHeaders()) {
                if (header.equals(colName))
                    colWidths.set(this.gridViewParameter.getHeaders().indexOf(header), colWidth);
            }
            tableInfo.setWidths(colWidths.toString());
            if (bSavedWidth) {
                tableInfoService.save(tableInfo);
            }
        });

        grid.setAutoSave(true);
        grid.setHeaderHeight(50);
        grid.setSizeFull();
//        grid.setTableWidth(500);
//        grid.setTableHeight(750);
    }

    public void setContextMenu(AATContextMenu contextMenu) {
        contextMenu.setTarget(grid);
        grid.setContextMenu(contextMenu);
    }

    private List<Item> getTableData(Object[] parameters, boolean flushRecords) throws Exception {

        List<Item> TableData = new ArrayList<>();

        if (!gridViewParameter.isValid()) {
            throw new Exception("TuiGrid Definition is not valid.");
        }
        if (gridViewParameter.isRequireParameter() && parameters == null) {
            throw new Exception("Parameters are required, but not set");
        }

        StringBuilder query = new StringBuilder("SELECT p.").append(gridViewParameter.getPrimaryIdFieldName());
        for (String header : gridViewParameter.getHeaders()) {
            query.append(", p.").append(header);
        }

        query.append(" FROM ").append(gridViewParameter.getFromDefinition()).append(" as p");

        if (gridViewParameter.getWhereDefinition() != null && (int) parameters[0] != -1) {
            query.append(" WHERE ").append("p.").append(gridViewParameter.getWhereDefinition());
            //TODO -set parameter
            query.append(" = ").append(parameters[0]);
        }
        for (Object[] data :
                service.findEntityByQuery(query.toString())) {
            List<String> recordData = Arrays.stream(data)
                    .map(obj -> {
                        if (obj instanceof ZJTEntity) {
                            return String.valueOf(((ZJTEntity) obj).getId());
                        } else {
                            return Objects.toString(obj, "");
                        }
                    })
                    .collect(Collectors.toList());
            GuiItem item = new GuiItem(Integer.parseInt(recordData.get(0)), recordData.subList(1, recordData.size()), gridViewParameter.getHeaders());
            TableData.add(item);
        }

        return TableData;
    }

    @SuppressWarnings("unchecked")
    private List<com.vaadin.componentfactory.tuigrid.model.Column> getColumns() {
        List<com.vaadin.componentfactory.tuigrid.model.Column> columns = new ArrayList<>();
        int nId = 0;

        List<Integer> colWidths = getColumnWidths();

        for (String header : this.gridViewParameter.getHeaders()) {
            String headerName = this.gridViewParameter.getHeaderNames().get(header);
            ColumnBaseOption baseOption = new ColumnBaseOption(nId++, headerName, header, colWidths.get(this.gridViewParameter.getHeaders().indexOf(header)), "center", "");
            com.vaadin.componentfactory.tuigrid.model.Column column = new com.vaadin.componentfactory.tuigrid.model.Column(baseOption);
            column.setEditable(true);
            column.setSortable(true);
            column.setSortingType("asc");
            int index = 1;
            if (header.equals("id"))
                continue;
            switch (this.gridViewParameter.getHeaderOptions().get(header)) {
                case "input":
                    column.setType("input");
                    break;
                case "check":
                    column.setType("check");
                    break;
                case "date":
                    column.setType("datePicker");
                    column.setDateOption(new DateOption("yyyy-MM-dd HH:mm A", true));
                    break;
                case "select_enum":
                    column.setType("select");
                    column.setRoot(true);
                    column.setTarget("");
                    List<RelationOption> elementsList = new ArrayList<>();
                    Class<?> fieldEnum = this.gridViewParameter.getHeaderTypeOptions().get(header);
                    for (Enum<?> elementList : getEnumConstants(fieldEnum)) {
                        RelationOption option = new RelationOption(elementList.toString(), String.valueOf(index++));
                        elementsList.add(option);
                    }
                    column.setRelationOptions(elementsList);
                    break;
                case "select_class":
                    column.setType("select");
                    column.setTarget("");
                    List<ZJTEntity> results = (List<ZJTEntity>) GlobalData.listData.get(header);
                    column.setRoot(!results.isEmpty());
                    List<RelationOption> options = new ArrayList<>();
                    for (Object result : results) {
                        Class<?> currentClass = result.getClass();
                        while (currentClass != null) {
                            try {
                                for (Field field : currentClass.getDeclaredFields()) {
                                    for (Annotation annotation : field.getAnnotations()) {
                                        if (annotation.annotationType().getName().equals(ContentDisplayedInSelect.class.getName())) {
                                            field.setAccessible(true);
                                            String name = (String) field.get(result);
                                            try {

                                                RelationOption option = new RelationOption(name, String.valueOf(((T) GlobalData.convertToZJTEntity(result, this.getClass().getClassLoader().loadClass(result.getClass().getName()))).getId()));
                                                options.add(option);
                                            } catch (RuntimeException e) {
                                                e.fillInStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            } catch (IllegalAccessException ignored) {
                            }
                            currentClass = currentClass.getSuperclass();
                        }
                    }
                    column.setRelationOptions(options);
                    break;
                default:
                    break;
            }
            columns.add(column);
        }
        return columns;
    }

    private void getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        btnReload.addClickListener(e -> reloadGrid());
        btnSave.addClickListener(e -> saveAll());
        toolbar.add(btnReload, btnSave);
        String filteredValue = "";
        if (this.gridViewParameter.getWhereDefinition() != null)
            for (Object data :
                    GlobalData.listData.get(this.gridViewParameter.getGroupName())) {
                Field pkFiled = GlobalData.getPrimaryKeyField(data.getClass());
                pkFiled.setAccessible(true);
                try {
                    if ((int) pkFiled.get(data) == (int) this.gridViewParameter.getParameters()[0])
                        filteredValue = GlobalData.getContentDisplayedInSelect(data);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        Span sp = new Span(">> " + filteredValue);
        Button btnGoOriginView = new Button(GlobalData.convertToStandard(this.gridViewParameter.groupName));
        routeLayout = new HorizontalLayout(btnGoOriginView, sp);
        routeLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        btnGoOriginView.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", this.gridViewParameter.getGroupClass().getName());
            String previousView = (String) VaadinSession.getCurrent().getAttribute("previousView");
            if (previousView != null) {
                UI.getCurrent().navigate(previousView);
            }
        });
        btnGoOriginView.getElement().setAttribute("theme", "tertiary-inline");
        btnGoOriginView.addClassName("link-button");

        // Set visibility based on isVehicle
        boolean bFilter = false;
        if (this.gridViewParameter.getParameters() != null &&
                (int) this.gridViewParameter.getParameters()[0] != -1
        )
            bFilter = (int) this.gridViewParameter.getParameters()[0] != -1;
        filterText.setVisible(!bFilter);
        routeLayout.setVisible(bFilter);

        columns = new Button("Columns");
        columns.addClickListener(e -> {
            selectedItems = twinColSelect.getSelectedItems();
            twinColSelDialog.open();
        });
        Checkbox autoWidthSave = new Checkbox("Save Column Width");
        autoWidthSave.setValue(bSavedWidth);
        autoWidthSave.addValueChangeListener(e -> bSavedWidth = e.getValue());
        HorizontalLayout columnToolbar = new HorizontalLayout(autoWidthSave, columns);
        columnToolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.add(columnToolbar);
        toolbar.addClassName("aat-toolbar");
    }

    public void setMessageStatus(String msg) {
        lblMessage.setValue(msg);
    }

    public void addCustomButton(Button button) {
        toolbar.add(button);
    }

    private void save(T row, String header, String colValue) {
        try {
            Field field = row.getClass().getDeclaredField(header);
            field.setAccessible(true);
            switch (this.gridViewParameter.getHeaderOptions().get(header)) {
                case "check":
                case "input":
                    String fieldType = field.getType().getSimpleName();
                    try {
                        switch (fieldType) {
                            case "Integer":
                            case "int":
                                field.set(row, Integer.parseInt(colValue));
                                break;
                            case "Double":
                            case "double":
                                field.set(row, Double.parseDouble(colValue));
                                break;
                            case "Float":
                            case "float":
                                field.set(row, Float.parseFloat(colValue));
                                break;
                            case "Boolean":
                            case "boolean":
                                field.set(row, Boolean.parseBoolean(colValue));
                                break;
                            case "LocalDate":
                                field.set(row, LocalDate.now());
                                break;
                            default:
                                field.set(row, colValue); // Fallback for String and other types
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Cannot parse to " + fieldType + ": " + colValue);
                    }
                    break;
                case "date":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
                    try {
                        LocalDateTime dateTime = LocalDateTime.now();
                        if (!colValue.isEmpty())
                            dateTime = LocalDateTime.parse(colValue, formatter);
                        field.set(row, dateTime);
                    } catch (DateTimeParseException ignored) {
                    }
                    break;
                case "select_enum":
                    Class<?> enumTypes = this.gridViewParameter.getHeaderTypeOptions().get(header);
                    if (enumTypes.isEnum()) {
                        Enum<?>[] enumConstants = getEnumConstants(enumTypes);
                        int ordinal = Integer.parseInt(colValue.substring(0, 1)) - 1;
                        if (ordinal >= 0 && ordinal < enumConstants.length) {
                            Enum<?> enumValue = enumConstants[ordinal];
                            field.set(row, enumValue);
                        }
                    }
                    break;
                case "select_class":
                    if (colValue.equals("All")) break;

                    int ordinal = -1;

                    if (!colValue.isEmpty()) ordinal = Integer.parseInt(colValue.substring(0, 1)) - 1;

                    Object dataSel = field.get(row);
                    if (dataSel == null) dataSel = field.getType().getDeclaredConstructor().newInstance();

                    int index = 0;

                    for (Object result : GlobalData.listData.get(header)) {
                        try {
                            if (index++ == ordinal) {
                                field.set(row, GlobalData.convertToZJTEntity(result, dataSel.getClass()));
                                break;
                            }
                        } catch (RuntimeException ignored) {
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void onNewRecord(GuiItem item) {
        T row;
        try {
            row = (T) this.gridViewParameter.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (String header : item.getHeaders()) {
            String colValue = item.getRecordData().get(item.getHeaders().indexOf(header));
            this.save(row, header, colValue);
        }

        T finalRow = row;
        grid.setRowCountOnElement("rowcount");
        CompletableFuture.runAsync(() -> service.save(finalRow));
    }

    public int onCellUpdate(Object[] parameters) throws Exception {
        if (!gridViewParameter.isValid()) {
            throw new Exception("TuiGrid Definition is not valid.");
        }
        if (gridViewParameter.isRequireParameter() && parameters == null) {
            throw new Exception("Parameters are required, but not set");
        }

        StringBuilder query = new StringBuilder("Update ").append(gridViewParameter.getFromDefinition());
        query.append(" p SET p.")
                .append(parameters[1]).append(" = ")
                .append(":param1");
        query.append(" WHERE ")
                .append("p.").append(gridViewParameter.getPrimaryIdFieldName())
                .append(" = ")
                .append(":param2");

        return service.updateEntityByQuery(query.toString(), parameters);
    }

    public void onDeleteRecordChecked() {
        int[] checkedItems = grid.getCheckedItems();
        for (int checkedRow : checkedItems) {
            service.delete(tableData.get(checkedRow));
        }
        grid.setRowCountOnElement("rowcount");
    }

    private void saveAll() {
    }

    private void reloadGrid() {
        grid.reloadData();
    }

    private List<Integer> getColumnWidths() {
        String strWidths = tableInfo.getWidths();
        List<Integer> colWidths = new ArrayList<>();

        if (strWidths == null || strWidths.isEmpty() || strWidths.equals("[]")) {
            for (String ignored : this.gridViewParameter.getHeaders()) {
                colWidths.add(0);
            }
        } else {
            String[] elements = strWidths.substring(1, strWidths.length() - 1).split(", ");
            for (String element : elements) {
                colWidths.add(Integer.parseInt(element));
            }
        }

        return colWidths;
    }

    private void updateList() {
        try {
            grid.setItems(this.getTableData(new Integer[]{-1}, false));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        add(grid);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> Enum<T>[] getEnumConstants(Class<?> enumTypes) {
        return ((Class<T>) enumTypes).getEnumConstants();
    }
}