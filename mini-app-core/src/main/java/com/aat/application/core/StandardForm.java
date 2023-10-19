package com.aat.application.core;

import com.aat.application.util.GlobalData;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class StandardForm<T extends ZJTEntity, S extends ZJTService<T>> extends FormLayout {

    private static final long serialVersionUID = -5183438338263448739L;

    protected TextField filterText = new TextField();
    protected Button save;
    protected Button close;
    protected TuiGrid grid;
    protected S service;
    List<String> headers;
    Dictionary<String, String> headerOptions = new Hashtable<>();
    Dictionary<String, Class<?>> headerTypeOptions = new Hashtable<>();
    List<Item> items = new ArrayList<>();
    List<T> tableData = new ArrayList<>();

    public StandardForm(Class<T> entityClass, S service) {
        addClassName("demo-app-form");
        this.service = service;

//        binder = new BeanValidationBinder<>(entityClass);
        save = new Button("Save");
        close = new Button("Cancel");

        headers = configureHeader(entityClass);
        configureGrid(entityClass);

        add(getToolbar(), grid);

//        binder.bindInstanceFields(this);
    }

    private List<String> configureHeader(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (int i = 1; i < fields.length; i++) {
            if (fields[i].getAnnotation(jakarta.persistence.Column.class) != null) {

                fieldNames.add(fields[i].getName());
                headerOptions.put(fields[i].getName(), "input");
            }
            if (fields[i].getAnnotation(jakarta.persistence.Enumerated.class) != null) {
                fieldNames.add(fields[i].getName());
                headerTypeOptions.put(fields[i].getName(), fields[i].getType());
                headerOptions.put(fields[i].getName(), "select_enum");
            }
            if (fields[i].getAnnotation(jakarta.persistence.JoinColumn.class) != null) {
                fieldNames.add(fields[i].getName());
                headerOptions.put(fields[i].getName(), "select_class");
            }
        }

        return fieldNames;
    }

    private void configureGrid(Class<T> entityClass) {
        grid = new TuiGrid();
        grid.addClassName("scheduler-grid");

        grid.setHeaders(headers);

        grid.setColumns(this.getColumns());

        items = this.getTableData();
        grid.setItems(items);

        grid.setRowHeaders(List.of("checkbox"));

        Theme inputTheme = new Theme();
        inputTheme.setMaxLength(10);
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
                    if (tableData.size() == 0)
                        tableData.add(entityClass.getDeclaredConstructor().newInstance());
                    else {
                        T row = tableData.get(0);
                        tableData.add((T) row.getClass().getDeclaredConstructor().newInstance());
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
                                Enum<?>[] enumConstants = ((Class<Enum<?>>) enumTypes).getEnumConstants();
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
                                } catch (RuntimeException | IllegalAccessException e3) {
                                    e3.printStackTrace();
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

            // Asynchronously save the modified row
            CompletableFuture.runAsync(() -> {
                service.save(row);
            });
        });
        grid.addItemDeleteListener(listener -> {
            delete();
        });

        grid.setAutoSave(true);
        grid.setSizeFull();
        grid.setHeaderHeight(50);
        grid.setTableWidth(500);
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

        Enumeration<String> e = headerOptions.keys();
        while (e.hasMoreElements()) {
            String header = e.nextElement();
            String headerName = header.substring(0, 1).toUpperCase()
                    + header.substring(1);
            ColumnBaseOption baseOption =
                    new ColumnBaseOption(nId++, headerName, header, 250, "center", "");
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
                    for (Enum elementList : ((Class<Enum>) fieldEnum).getEnumConstants()) {
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
                    List<ZJTEntity> results = GlobalData.listData.get(headerName);
                    List<RelationOption> options = new ArrayList<>();
                    for (Object result : results) {
                        try {
                            Field nameField = result.getClass().getDeclaredField("name");
                            nameField.setAccessible(true);
                            String name = (String) nameField.get(result);
                            RelationOption option = new RelationOption(name, String.valueOf(index++));
                            options.add(option);
                        } catch (NoSuchFieldException | IllegalAccessException e3) {
                            e3.printStackTrace();
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

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterText);

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
        if (grid.getCheckedItems().length == 0)
            return;
        for (int checkedRow :
                grid.getCheckedItems()) {
            service.delete(tableData.get(checkedRow));
        }
    }
//
//    // Events
//    public static abstract class StandardFormEvent<T extends ZJTEntity, S extends ZJTService<T>> extends ComponentEvent<StandardForm<T, S>> {
//        private final T bean;
//
//        protected StandardFormEvent(StandardForm<T, S> source, T bean) {
//            super(source, false);
//            this.bean = bean;
//        }
//
//        public T getBean() {
//            return bean;
//        }
//    }
//
//    public static class SaveEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
//        SaveEvent(StandardForm<T, S> source, T bean) {
//            super(source, bean);
//        }
//    }
//
//    public static class CloseEvent<T extends ZJTEntity, S extends ZJTService<T>> extends StandardFormEvent<T, S> {
//        CloseEvent(StandardForm<T, S> source) {
//            super(source, null);
//        }
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent<T, S>> listener) {
//        return addListener(SaveEvent.class, (ComponentEventListener) listener);
//    }
//
//    public Registration addCloseListener(ComponentEventListener<CloseEvent<T, S>> listener) {
//        return addListener(CloseEvent.class, (ComponentEventListener) listener);
//    }
}