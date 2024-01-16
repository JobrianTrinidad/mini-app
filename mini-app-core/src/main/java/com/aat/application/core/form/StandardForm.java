package com.aat.application.core.form;

import com.aat.application.annotations.ContentDisplayedInSelect;
import com.aat.application.annotations.DisplayName;
import com.aat.application.core.component.AATTwinColSelect;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.core.event.EventBus;
import com.aat.application.data.entity.ZJTTableInfo;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import jakarta.persistence.Id;

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

    protected TableInfoService tableInfoService;
    Class<T> entityClass;
    Class<T> filteredEntityClass;
    String fieldDisplayedInSelect;
    private ZJTTableInfo tableInfo;
    protected TextField filterText = new TextField();
    private final Button btnReload = new Button("Reload");
    private final Button btnSave = new Button("Save");
    private String groupName = "";
    protected Button columns;
    private Dialog twinColSelDialog;
    AATTwinColSelect twinColSelect;
    Set<String> selectedItems;
    protected TuiGrid grid;
    protected S service;
    List<String> headers;
    LinkedHashMap<String, String> headerOptions = new LinkedHashMap<>();
    Dictionary<String, String> headerNames = new Hashtable<>();
    Dictionary<String, Class<?>> headerTypeOptions = new Hashtable<>();
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();
    private boolean bSavedWidth = false;
    private VerticalLayout verticalLayout;
    private HorizontalLayout toolbar;

    public StandardForm(Class<T> entityClass, Class<T> filteredEntityClass,
                        S service, TableInfoService tableInfoService,
                        String groupName, int filterObjectId) {
        addClassName("demo-app-form");
        this.service = service;
        this.tableInfoService = tableInfoService;
        this.entityClass = entityClass;
        this.filteredEntityClass = filteredEntityClass;
        this.groupName = groupName;

        headers = configureHeader(entityClass);

        initColSelDialog(entityClass, groupName, filterObjectId);

        loadGrid(entityClass, groupName, filterObjectId);
        EventBus.getInstance().register(event -> {
            if ("DrawerToggleClicked".equals(event)) {
                getUI().ifPresent(ui -> ui.access(grid::refreshGrid));
            }
        });
    }

    private void loadGrid(Class<T> entityClass, String groupName, int filterObjectId) {
//        removeAll();
        if (!twinColSelect.getSelectedItems().isEmpty()) configureGrid(entityClass, groupName, filterObjectId);
        toolbar = getToolbar(groupName, filterObjectId);
        verticalLayout = new VerticalLayout(toolbar);
        if (grid != null) add(verticalLayout, grid);
        else add(new VerticalLayout(verticalLayout));
    }

    private void initColSelDialog(Class<T> entityClass, String groupName, int filterObjectId) {
        twinColSelect = new AATTwinColSelect();
        List<String> tempHeaderNames = new ArrayList<>();
        for (String header : headers) {
            tempHeaderNames.add(headerNames.get(header));
        }

        twinColSelect.setItems(tempHeaderNames);
//        twinColSelect.setLabel("Select Options");

        tableInfo = tableInfoService.findByTableName(entityClass.getSimpleName());
        if (tableInfo == null) {
            tableInfo = new ZJTTableInfo();
            tableInfo.setTable_name(entityClass.getSimpleName());
        }
        String tempHeader = tableInfo.getHeaders();
        List<String> tempDisplayedHeaderNames = tempHeaderNames;
        if (tempHeader != null && !tempHeader.equals("[]")) {
            headers = Arrays.stream(tempHeader.substring(1, tempHeader.length() - 1).split(",")).map(String::trim).collect(Collectors.toList());
            tempDisplayedHeaderNames = new ArrayList<>();
            for (String header : headers) {
                tempDisplayedHeaderNames.add(headerNames.get(header));
            }
        }

        twinColSelect.select(tempDisplayedHeaderNames);


        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");
        HorizontalLayout btnPanel = new HorizontalLayout(btnCancel, btnOk);
        btnPanel.setAlignItems(FlexComponent.Alignment.END);

        ZJTTableInfo finalTableInfo = tableInfo;
        btnOk.addClickListener(e -> {
            List<String> originHeaders = headers;
            List<Integer> columnWidths = getColumnWidths();
            List<Integer> allowedWidths = new ArrayList<>();
            List<String> tempTwinItems = new ArrayList<>(twinColSelect.getSelectedItems());
            List<String> tempHeaders = new ArrayList<>();
            if (!tempTwinItems.contains(fieldDisplayedInSelect)
                    && fieldDisplayedInSelect != null)
                tempTwinItems.add(fieldDisplayedInSelect);
            twinColSelect.select(tempTwinItems);
            for (String desiredValue : tempTwinItems) {
                Enumeration<String> keys = headerNames.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();

                    if (headerNames.get(key).equals(desiredValue)) {
                        tempHeaders.add(key);
                        break;
                    }
                }
            }
            headers = tempHeaders;

            for (String allowedHeader : headers) {
                if (originHeaders.contains(allowedHeader)) {
                    allowedWidths.add(columnWidths.get(originHeaders.indexOf(allowedHeader)));
                } else allowedWidths.add(0);
            }

            finalTableInfo.setWidths(allowedWidths.toString());
            finalTableInfo.setHeaders(headers.toString());
            tableInfoService.save(finalTableInfo);
            tableInfo = finalTableInfo;

            if (!headers.isEmpty()) this.loadGrid(entityClass, groupName, filterObjectId);
            else grid.removeFromParent();

            twinColSelDialog.close();
        });

        btnCancel.addClickListener(e -> twinColSelDialog.close());

        twinColSelDialog = new Dialog();
        twinColSelDialog.add(new VerticalLayout(twinColSelect, btnPanel));
        twinColSelDialog.setCloseOnEsc(true);
        twinColSelDialog.setCloseOnOutsideClick(true);
    }

    private List<String> configureHeader(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                fieldNames.add("id");
                headerNames.put("id", "ID");
            }
            if (field.getAnnotation(DisplayName.class) == null) {
                continue;
            }
            if (field.getAnnotation(ContentDisplayedInSelect.class) != null) {
                fieldDisplayedInSelect = field.getName();
            }
            if (field.getAnnotation(jakarta.persistence.Column.class) != null) {
                fieldNames.add(field.getName());
                if (field.getType().getSimpleName().equals("LocalDateTime"))
                    headerOptions.put(field.getName(), "date");
                else
                    headerOptions.put(field.getName(), "input");
                headerNames.put(field.getName(), field.getAnnotation(DisplayName.class).value());
            }
            if (field.getAnnotation(jakarta.persistence.Enumerated.class) != null) {
                fieldNames.add(field.getName());
                headerTypeOptions.put(field.getName(), field.getType());
                headerNames.put(field.getName(), field.getAnnotation(DisplayName.class).value());
                headerOptions.put(field.getName(), "select_enum");
            }
            if (field.getAnnotation(jakarta.persistence.JoinColumn.class) != null) {
                fieldNames.add(field.getName());
                headerNames.put(field.getName(), field.getAnnotation(DisplayName.class).value());
                headerOptions.put(field.getName(), "select_class");
                GlobalData.addData(field.getName(), (Class<? extends ZJTEntity>) field.getType());
            }
        }

        return fieldNames;
    }

    private void configureGrid(Class<T> entityClass, String groupName, int filterObjectId) {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(headers);

        String fieldName = "";
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getName().equals(groupName)) {
                for (Field childField : field.getType().getDeclaredFields()) {
                    if (childField.getAnnotation(Id.class) != null) {
                        fieldName = groupName + "." + childField.getName();
                    }
                }
            }
        }

        items = this.getTableData(fieldName, filterObjectId);
        grid.setItems(items);

        List<Column> columns = this.getColumns();
        grid.setColumns(columns);

        List<Summary> summaries = List.of(
                new Summary(columns.get(columns.size() - 1).getColumnBaseOption().getName(), Summary.OperationType.rowcount));

        grid.setSummaries(summaries);
        grid.setHeaderHeight(100);
        grid.setSummaryHeight(40);

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

            for (String header : headers) {
                if (header.equals(colName)) colWidths.set(headers.indexOf(header), colWidth);
            }
            tableInfo.setWidths(colWidths.toString());
            if (bSavedWidth) {
                tableInfoService.save(tableInfo);
            }
        });

        grid.addItemChangeListener(event -> {
            items = grid.getItems();
            if (filterText != null) tableData = service.findAll(filterText.getValue());
            else tableData = service.findAll(null);

            Comparator<T> comparator = Comparator.comparing(ZJTEntity::getId);
            tableData.sort(comparator);

            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int columnIndex = item.getHeaders().indexOf(colName);
            if (event.getRow() >= tableData.size()) {
                try {
                    if (tableData.isEmpty()) tableData.add(entityClass.getDeclaredConstructor().newInstance());
                    else {
                        T newRow = (T) tableData.get(0).getClass().getDeclaredConstructor().newInstance();
                        tableData.add(newRow);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            T row = tableData.get(event.getRow());
            if (columnIndex >= 0) {
                this.save(row, colName, event.getColValue());
                CompletableFuture.runAsync(() -> service.save(row));
            }
        });
        grid.addItemDeleteListener(listener -> delete());
        grid.addItemAddListener(event -> {
            try {
                T row = entityClass.getDeclaredConstructor().newInstance();
                this.save(row, (GuiItem) event.getItem());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        grid.setAutoSave(true);
        grid.setSizeFull();
        grid.setHeaderHeight(50);
//        grid.setTableWidth(500);
//        grid.setTableHeight(750);
    }

    public void setContextMenu(AATContextMenu contextMenu) {
        contextMenu.setTarget(grid);
        grid.setContextMenu(contextMenu);
    }

    private void save(T row, String header, String colValue) {
        try {
            Field field = row.getClass().getDeclaredField(header);
            field.setAccessible(true);
            switch (headerOptions.get(header)) {
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
                            case "LocalDateTime":
                                LocalDate date = LocalDate.parse(colValue);
                                LocalDateTime dateTime = date.atStartOfDay();
                                field.set(row, dateTime);
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
                        LocalDateTime dateTime = LocalDateTime.parse(colValue, formatter);
                        field.set(row, dateTime);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "select_enum":
                    Class<?> enumTypes = headerTypeOptions.get(header);
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

    private void save(T row, GuiItem item) {
        for (String header : item.getHeaders()) {
            String colValue = item.getRecordData().get(item.getHeaders().indexOf(header));
            this.save(row, header, colValue);
        }

        CompletableFuture.runAsync(() -> service.save(row));
    }

    private List<Item> getTableData(String fieldName, int filterId) {

        List<Item> TableData = new ArrayList<>();
        if (filterId == -1) {
            if (filterText != null) tableData = service.findAll(filterText.getValue());
            else tableData = service.findAll(null);
        } else tableData = findRecordsByField(fieldName, filterId);

        Comparator<T> comparator = Comparator.comparing(ZJTEntity::getId);
        tableData.sort(comparator);

        for (T data : tableData) {
            List<String> rowData = new ArrayList<>(Arrays.asList(new String[headers.size()]));
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                try {
                    if (header.equals("id"))
                        continue;
                    String headerName = header.substring(0, 1).toLowerCase() + header.substring(1);
                    Field headerField = data.getClass().getDeclaredField(headerName);
                    headerField.setAccessible(true);
                    Object dataSel = headerField.get(data);
                    switch (headerOptions.get(header)) {
                        case "input":
                        case "date":
                            rowData.set(i, dataSel != null ? dataSel.toString() : "");
                            break;
                        case "select_enum":
                            rowData.set(i, String.valueOf(((Enum<?>) dataSel).ordinal() + 1));
                            break;
                        case "select_class":
                            int index = 0;
                            if (dataSel == null) dataSel = headerField.getType().getDeclaredConstructor().newInstance();
                            Field pkFieldDataSel = GlobalData.getPrimaryKeyField(dataSel.getClass());
                            pkFieldDataSel.setAccessible(true);
                            for (Object result : GlobalData.listData.get(header)) {
                                Field pkField = GlobalData.getPrimaryKeyField(result.getClass());
                                pkField.setAccessible(true);
                                index++;
                                if ((int) pkFieldDataSel.get(dataSel) == (int) pkField.get(result)) {
                                    rowData.set(i, String.valueOf(index));
                                    break;
                                }
                            }
                            break;
                        default:
                            rowData.set(i, "");
                            break;
                    }

                } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException |
                         InstantiationException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                for (Field field : data.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getAnnotation(Id.class) != null) {
                        TableData.add(new GuiItem((Integer) field.get(data), rowData, headers));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return TableData;
    }

    public <T> List<T> findRecordsByField(String fieldName, int filterId) {
        return service.findRecordsByFieldId(fieldName, filterId);
    }

    private List<com.vaadin.componentfactory.tuigrid.model.Column> getColumns() {
        List<com.vaadin.componentfactory.tuigrid.model.Column> columns = new ArrayList<>();
        int nId = 0;

        List<Integer> colWidths = getColumnWidths();

        for (String header : headers) {
            String headerName = headerNames.get(header);
            ColumnBaseOption baseOption = new ColumnBaseOption(nId++, headerName, header, colWidths.get(headers.indexOf(header)), "center", "");
            com.vaadin.componentfactory.tuigrid.model.Column column = new com.vaadin.componentfactory.tuigrid.model.Column(baseOption);
            new Column(new ColumnBaseOption(4, "Date-TimePicker With tab", "timepickerwithtab", 150, "center", ""), true, "datePicker", new DateOption("yyyy-MM-dd HH:mm A", true, "tab", "spinbox"));
            column.setEditable(true);
            column.setSortable(true);
            column.setSortingType("asc");
            int index = 1;
            if (header.equals("id"))
                continue;
            switch (headerOptions.get(header)) {
                case "input":
                    column.setType("input");
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
                    Class<?> fieldEnum = headerTypeOptions.get(header);
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
                                            RelationOption option = new RelationOption(name, String.valueOf(index++));
                                            options.add(option);
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

    private HorizontalLayout getToolbar(String beforeRouteName, int filterObjectId) {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        btnReload.addClickListener(e -> reloadGrid());
        btnSave.addClickListener(e -> saveAll());
        HorizontalLayout baseLayout = new HorizontalLayout(filterText, btnReload, btnSave);
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
        Button btnGoOriginView = new Button(GlobalData.convertToStandard(beforeRouteName));
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

        // Set visibility based on isVehicle
        boolean isFilter = filterObjectId != -1;
        baseLayout.setVisible(!isFilter);
        routeLayout.setVisible(isFilter);

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
        HorizontalLayout toolbar = new HorizontalLayout(baseLayout, routeLayout, columnToolbar);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        toolbar.addClassName("aat-toolbar");

        return toolbar;
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
            for (String ignored : headers) {
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
        grid.setItems(this.getTableData("", -1));
        add(grid);
    }

    private void delete() {
        int[] checkedItems = grid.getCheckedItems();
        for (int checkedRow : checkedItems) {
            service.delete(tableData.get(checkedRow));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> Enum<T>[] getEnumConstants(Class<?> enumTypes) {
        return ((Class<T>) enumTypes).getEnumConstants();
    }
}