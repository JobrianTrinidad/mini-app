package com.aat.application.core.form;

import com.aat.application.annotations.DisplayName;
import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTTableInfo;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.tatu.TwinColSelect;

import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -5183438338263448739L;

    protected TableInfoService tableInfoService;
    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    protected Button columns;
    private Dialog twinColSelDialog;
    TwinColSelect<String> twinColSelect;
    Set<String> selectedItems;
    protected TuiGrid grid;
    protected S service;
    List<String> headers;
    LinkedHashMap<String, String> headerOptions = new LinkedHashMap<>();
    Dictionary<String, String> headerNames = new Hashtable<>();
    Dictionary<String, Class<?>> headerTypeOptions = new Hashtable<>();
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();

    public StandardForm(Class<T> entityClass, S service, TableInfoService tableInfoService) {
        addClassName("demo-app-form");
        this.service = service;
        this.tableInfoService = tableInfoService;

//        binder = new BeanValidationBinder<>(entityClass);
        save = new Button("Save");
        close = new Button("Cancel");

        headers = configureHeader(entityClass);

        initColSelDialog(entityClass);

        loadGrid(entityClass);
    }

    private void loadGrid(Class<T> entityClass) {
        removeAll();
        if (!twinColSelect.getSelectedItems().isEmpty())
            configureGrid(entityClass);
        if (grid != null)
            add(new VerticalLayout(getToolbar(entityClass), grid));
        else
            add(new VerticalLayout(getToolbar(entityClass)));
    }

    private void initColSelDialog(Class<T> entityClass) {
        twinColSelect = new TwinColSelect<>();
        twinColSelect.setItems(headers);
        twinColSelect.setLabel("Select Options");

        ZJTTableInfo tableInfo = tableInfoService.findByTableName(entityClass.getSimpleName());
        if (tableInfo == null) {
            tableInfo = new ZJTTableInfo();
            tableInfo.setTable_name(entityClass.getSimpleName());
        }
        String tempHeader = tableInfo.getHeaders();
        headers = Arrays.stream(tempHeader.substring(1, tempHeader.length() - 1).split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        twinColSelect.select(headers);

        Checkbox autoWidthSave = new Checkbox("Save Column Width");
        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");
        HorizontalLayout btnPanel = new HorizontalLayout(btnCancel, btnOk);
        btnPanel.setAlignItems(FlexComponent.Alignment.END);

        ZJTTableInfo finalTableInfo = tableInfo;
        btnOk.addClickListener(e -> {
            headers = new ArrayList<>(twinColSelect.getSelectedItems());
            if (!headers.isEmpty())
                this.loadGrid(entityClass);
            else
                grid.removeFromParent();

            finalTableInfo.setHeaders(headers.toString());
            tableInfoService.save(finalTableInfo);

            twinColSelDialog.close();
        });

        btnCancel.addClickListener(e -> {
            twinColSelect.deselectAll();
            twinColSelect.select(selectedItems);
            twinColSelDialog.close();
        });

        twinColSelDialog = new Dialog();
        twinColSelDialog.add(autoWidthSave, new VerticalLayout(twinColSelect, btnPanel));
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
            }
        }

        return fieldNames;
    }

    private void configureGrid(Class<T> entityClass) {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");
        grid.setHeaders(headers);

        items = this.getTableData();
        grid.setItems(items);

        grid.setColumns(this.getColumns());

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

        grid.addItemChangeListener(event -> {
            items = grid.getItems();
            if (filterText != null)
                tableData = service.findAll(filterText.getValue());
            else
                tableData = service.findAll(null);

            Comparator<T> comparator = Comparator.comparing(ZJTEntity::getName);
            tableData.sort(comparator);

            GuiItem item = (GuiItem) items.get(event.getRow());
            String colName = event.getColName();
            int columnIndex = item.getHeaders().indexOf(colName);
            if (event.getRow() >= tableData.size()) {
                try {
                    if (tableData.isEmpty())
                        tableData.add(entityClass.getDeclaredConstructor().newInstance());
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
                String header = headers.get(columnIndex);
                try {
                    Field field = row.getClass().getDeclaredField(header);
                    field.setAccessible(true);
                    switch (headerOptions.get(header)) {
                        case "input":
                            field.set(row, event.getColValue());
                            break;
                        case "select_enum":
                            Class<?> enumTypes = headerTypeOptions.get(header);
                            if (enumTypes.isEnum()) {
                                Enum<?>[] enumConstants = getEnumConstants(enumTypes);
                                int ordinal = Integer.parseInt(event.getColValue().substring(0, 1)) - 1;
                                if (ordinal >= 0 && ordinal < enumConstants.length) {
                                    Enum<?> enumValue = enumConstants[ordinal];
                                    field.set(row, enumValue);
                                }
                            }
                            break;
                        case "select_class":
                            String headerName = header.substring(0, 1).toUpperCase()
                                    + header.substring(1);
                            int ordinal = Integer.parseInt(event.getColValue().substring(0, 1)) - 1;

                            Object dataSel = field.get(row);
                            if (dataSel == null)
                                dataSel = field.getType().getDeclaredConstructor().newInstance();
                            Field selField = dataSel.getClass().getDeclaredFields()[0];
                            selField.setAccessible(true);
                            int index = 0;

                            for (Object result : GlobalData.listData.get(headerName)) {
                                try {
                                    Field idField = result.getClass().getDeclaredFields()[0];
                                    idField.setAccessible(true);
                                    Object idObj = idField.get(result);
                                    if (index++ == ordinal) {
                                        selField.set(dataSel, idObj);
                                        field.set(row, dataSel);
                                        break;
                                    }
                                } catch (RuntimeException | IllegalAccessException ignored) {
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

            CompletableFuture.runAsync(() -> service.save(row));
        });
        grid.addItemDeleteListener(listener -> delete());

        grid.setAutoSave(true);
        grid.setSizeFull();
        grid.setHeaderHeight(50);
//        grid.setTableWidth(500);
//        grid.setTableHeight(750);
    }

    private List<Item> getTableData() {

        List<Item> TableData = new ArrayList<>();
        if (filterText != null)
            tableData = service.findAll(filterText.getValue());
        else
            tableData = service.findAll(null);

        Comparator<T> comparator = Comparator.comparing(ZJTEntity::getName);
        tableData.sort(comparator);

        for (T data :
                tableData) {
            List<String> rowData = new ArrayList<>(Arrays.asList(new String[headers.size()]));
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                try {
                    String headerName = header.substring(0, 1).toLowerCase()
                            + header.substring(1);
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
                            headerName = header.substring(0, 1).toUpperCase()
                                    + header.substring(1);
                            int index = 0;
                            if (dataSel == null)
                                dataSel = headerField.getType().getDeclaredConstructor().newInstance();
                            Field selField = dataSel.getClass().getDeclaredField("name");
                            selField.setAccessible(true);
                            String selName = (String) selField.get(dataSel);
                            for (Object result : GlobalData.listData.get(headerName)) {
                                Field nameField = result.getClass().getDeclaredField("name");
                                nameField.setAccessible(true);
                                String name = (String) nameField.get(GlobalData.listData.get(headerName).get(index++));
                                if (selName.equals(name)) {
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
            TableData.add(new GuiItem(
                    rowData,
                    headers));

        }
        return TableData;
    }

    private List<com.vaadin.componentfactory.tuigrid.model.Column> getColumns() {
        List<com.vaadin.componentfactory.tuigrid.model.Column> columns = new ArrayList<>();
        int nId = 0;

        for (String header : headers) {
            if (!headers.contains(header))
                continue;
            String headerName = headerNames.get(header);
            ColumnBaseOption baseOption =
                    new ColumnBaseOption(nId++, headerName, header, 0, "center", "");
            com.vaadin.componentfactory.tuigrid.model.Column column =
                    new com.vaadin.componentfactory.tuigrid.model.Column(baseOption);
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
                    column.setRoot(true);
                    column.setTarget("");
                    GlobalData.addData(headerName);
                    List<ZJTEntity> results = (List<ZJTEntity>) GlobalData.listData.get(headerName);
                    List<RelationOption> options = new ArrayList<>();
                    for (Object result : results) {
                        try {
                            Field nameField = result.getClass().getDeclaredField("name");
                            nameField.setAccessible(true);
                            String name = (String) nameField.get(result);
                            RelationOption option = new RelationOption(name, String.valueOf(index++));
                            options.add(option);
                        } catch (NoSuchFieldException | IllegalAccessException ignored) {
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

    private HorizontalLayout getToolbar(Class<T> entityClass) {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterText);

        columns = new Button("Columns");
        columns.addClickListener(e -> {
            selectedItems = twinColSelect.getSelectedItems();
            twinColSelDialog.open();
        });
        toolbar.add(columns);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void updateList() {
        grid.setItems(this.getTableData());
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