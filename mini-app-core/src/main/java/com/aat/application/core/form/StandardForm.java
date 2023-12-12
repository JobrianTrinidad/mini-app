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
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -5183438338263448739L;

    protected TableInfoService tableInfoService;
    Class<T> entityClass;
    Class<T> filteredEntityClass;
    String fieldDisplayedInSelect;
    private ZJTTableInfo tableInfo;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
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

    public StandardForm(Class<T> entityClass, S service, TableInfoService tableInfoService, boolean isFilter) {
        addClassName("demo-app-form");
        this.service = service;
        this.tableInfoService = tableInfoService;
        this.entityClass = entityClass;

//        binder = new BeanValidationBinder<>(entityClass);
        save = new Button("Save");
        close = new Button("Cancel");

        headers = configureHeader(entityClass);

        initColSelDialog(entityClass, isFilter);

        loadGrid(entityClass, isFilter);
        EventBus.getInstance().register(event -> {
            if ("DrawerToggleClicked".equals(event)) {
                    getUI().ifPresent(ui -> ui.access(grid::refreshGrid));
            }
        });
    }

    private void loadGrid(Class<T> entityClass, boolean isFilter) {
//        removeAll();
        if (!twinColSelect.getSelectedItems().isEmpty()) configureGrid(entityClass);
        if (grid != null) add(new VerticalLayout(getToolbar(entityClass, isFilter), grid));
        else add(new VerticalLayout(getToolbar(entityClass, isFilter)));
    }

    private void initColSelDialog(Class<T> entityClass, boolean isFilter) {
        twinColSelect = new AATTwinColSelect();
        List<String> tempHeaderNames = new ArrayList<>();
        for (String header :
                headers) {
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
            for (String header :
                    headers) {
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
            if (!tempTwinItems.contains(fieldDisplayedInSelect))
                tempTwinItems.add(fieldDisplayedInSelect);
            twinColSelect.select(tempTwinItems);
            for (String desiredValue :
                    tempTwinItems) {
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

            for (String allowedHeader :
                    headers) {
                if (originHeaders.contains(allowedHeader)) {
                    allowedWidths.add(columnWidths.get(originHeaders.indexOf(allowedHeader)));
                } else
                    allowedWidths.add(0);
            }

            finalTableInfo.setWidths(allowedWidths.toString());
            finalTableInfo.setHeaders(headers.toString());
            tableInfoService.save(finalTableInfo);
            tableInfo = finalTableInfo;

            if (!headers.isEmpty()) this.loadGrid(entityClass, isFilter);
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
            if (field.getAnnotation(DisplayName.class) == null) {
                continue;
            }
            if (field.getAnnotation(ContentDisplayedInSelect.class) != null) {
                fieldDisplayedInSelect = field.getAnnotation(ContentDisplayedInSelect.class).value();
            }
            if (field.getAnnotation(jakarta.persistence.Column.class) != null) {
                fieldNames.add(field.getName());
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

    private void configureGrid(Class<T> entityClass) {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(headers);

        items = this.getTableData(null, "", "");
        grid.setItems(items);

        List<Column> columns = this.getColumns();
        grid.setColumns(columns);

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

    public void setFilter(Class<T> filteredEntityClass, String fieldName, List<Cell> filter) {
        this.filteredEntityClass = filteredEntityClass;
        grid.setItems(this.getTableData(filteredEntityClass, fieldName, filter.get(0).getCellValue()));
        grid.refreshGrid();
//        List<T> filteredData = service.findRecordsByField(colName, filter);
//        grid.setItems(filteredData);
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
                            default:
                                field.set(row, colValue); // Fallback for String and other types
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where the string does not contain a parsable number
                        System.out.println("Cannot parse to " + fieldType + ": " + colValue);
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
                    if (colValue.equals("All"))
                        break;

                    int ordinal = -1;

                    if (!colValue.isEmpty())
                        ordinal = Integer.parseInt(colValue.substring(0, 1)) - 1;

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
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException |
                 InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(T row, GuiItem item) {
        for (String header :
                item.getHeaders()) {
            String colValue = item.getRecordData().get(item.getHeaders().indexOf(header));
            this.save(row, header, colValue);
        }

        CompletableFuture.runAsync(() -> service.save(row));
    }

    private List<Item> getTableData(Class<T> filterEntityClass, String fieldName, String filter) {

        List<Item> TableData = new ArrayList<>();
        if (fieldName.isEmpty()) {
            if (filterText != null) tableData = service.findAll(filterText.getValue());
            else tableData = service.findAll(null);
        } else
            tableData = findRecordsByField(filterEntityClass, fieldName, filter);

        Comparator<T> comparator = Comparator.comparing(ZJTEntity::getId);
        tableData.sort(comparator);

        for (T data : tableData) {
            List<String> rowData = new ArrayList<>(Arrays.asList(new String[headers.size()]));
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                try {
                    String headerName = header.substring(0, 1).toLowerCase() + header.substring(1);
                    Field headerField = data.getClass().getDeclaredField(headerName);
                    headerField.setAccessible(true);
                    Object dataSel = headerField.get(data);
                    switch (headerOptions.get(header)) {
                        case "input":
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
            TableData.add(new GuiItem(rowData, headers));

        }
        return TableData;
    }

    public <T> List<T> findRecordsByField(Class<T> filterEntityClass, String fieldName, String filter) {
        // Split the fieldName
        String[] fieldNames = fieldName.split("\\.");

        // Get the type of fieldName
        Class<?> fieldType = filterEntityClass;
        try {
            Field field = fieldType.getDeclaredField(fieldNames[1]);
            fieldType = field.getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // Convert filter to the type of fieldName
        Object convertedFilter;
        if (fieldType == String.class) {
            convertedFilter = filter;
        } else if (fieldType == Integer.class || fieldType == int.class) {
            convertedFilter = Integer.parseInt(filter);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            convertedFilter = Boolean.parseBoolean(filter);
        } else if (fieldType == Double.class || fieldType == double.class) {
            convertedFilter = Double.parseDouble(filter);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        }

        // Call the method with the converted filter
        return service.findRecordsByField(fieldName, convertedFilter);
    }

    private List<com.vaadin.componentfactory.tuigrid.model.Column> getColumns() {
        List<com.vaadin.componentfactory.tuigrid.model.Column> columns = new ArrayList<>();
        int nId = 0;

        List<Integer> colWidths = getColumnWidths();

        for (String header : headers) {
            String headerName = headerNames.get(header);
            ColumnBaseOption baseOption = new ColumnBaseOption(nId++, headerName, header, colWidths.get(headers.indexOf(header)), "center", "");
            com.vaadin.componentfactory.tuigrid.model.Column column = new com.vaadin.componentfactory.tuigrid.model.Column(baseOption);
            column.setEditable(true);
            column.setSortable(true);
            column.setSortingType("asc");
            int index = 1;
            switch (headerOptions.get(header)) {
                case "input":
                    column.setType("input");
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

    private HorizontalLayout getToolbar(Class<T> entityClass, boolean isFilter) {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button btnGoOriginView = new Button("Vehicle");
        btnGoOriginView.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("entityClass", this.filteredEntityClass.getName());
            UI.getCurrent().navigate("view/");
        });

        // Set visibility based on isVehicle
        filterText.setVisible(!isFilter);
        btnGoOriginView.setVisible(isFilter);

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
        HorizontalLayout toolbar = new HorizontalLayout(filterText, btnGoOriginView, columnToolbar);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        toolbar.addClassName("aat-toolbar");

        return toolbar;
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
        grid.setItems(this.getTableData(null, "", ""));
        add(grid);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, close);
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